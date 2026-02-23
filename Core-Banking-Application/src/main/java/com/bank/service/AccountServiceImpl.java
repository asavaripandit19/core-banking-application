package com.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.dto.AccountDisplayDTO;
import com.bank.dto.BalanceDTO;
import com.bank.dto.UpdateAccountDTO;
import com.bank.exception.AccountDetailsValidation;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidAmount;
import com.bank.mapper.AccountBalanceMapper;
import com.bank.mapper.AccountDisplayMapper;
import com.bank.models.Account;
import com.bank.models.CurrentAccount;
import com.bank.models.SavingAccount;
import com.bank.repository.AccountRepository;
import com.bank.repository.CurrentAccountRepository;
import com.bank.repository.SavingAccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountDetailsValidation adv;

	@Autowired
	private SavingAccountRepository savingAccountRepository;

	@Autowired
	private CurrentAccountRepository currentAccountRepository;

	//---------------------------------------CREATE ACCOUNT-----------------------------------------------------
	@Override
	public void createAccount(Account account) {
		adv.validName(account.getName());
		adv.validEmail(account.getEmail());
		adv.validMobileNumber(account.getMob());
		adv.validAadharNumber(account.getAadharNo());

		if (account instanceof SavingAccount) {

			SavingAccount savingAccount = (SavingAccount) account;

			if (account.getBalance() < savingAccount.getMIN_BALANCE())
				throw new InvalidAmount("You Should have to add at least " + savingAccount.getMIN_BALANCE() + "!");


		} else if (account instanceof CurrentAccount) {

			CurrentAccount currentAccount = (CurrentAccount) account;

			if (account.getBalance() < currentAccount.getMIN_BALANCE())
				throw new InvalidAmount("You Should have to add at least " + currentAccount.getMIN_BALANCE() + "!");

		}

		accountRepository.save(account);
	}

	
	//---------------------------------------DISPLAY ACCOUNTS-----------------------------------------------------
	@Override
	public List<AccountDisplayDTO> getAllAccounts() {

		return accountRepository.findAll().stream().map(AccountDisplayMapper::toSafeDto).toList();

	}

	@Override
	public List<AccountDisplayDTO> getSvaingAccounts() {

		return savingAccountRepository.findAll().stream().map(AccountDisplayMapper::toSafeDto).toList();
	}

	@Override
	public List<AccountDisplayDTO> getCurrentAccounts() {

		return currentAccountRepository.findAll().stream().map(AccountDisplayMapper::toSafeDto).toList();
	}

	
	//---------------------------------------CLOSE ACCOUNTS-----------------------------------------------------
	@Transactional
	@Override
	public String closeAccount(Long accno) {

		Account temp =accountRepository.findById(accno).orElseThrow(() -> new AccountNotFoundException("Account Not Found "));

//		if(temp.getBalance() < 0) {
//			temp.setBalance(0.0);
//			 accountRepository.save(temp);
//		}

		accountRepository.deleteById(accno);
		return "Account Close Successfully!!";
	}

	
	//---------------------------------------SERACH ACCOUNT-----------------------------------------------------
	@Override
	public Account getByAccountNumber(Long accno) {
		adv.validAccountNumber(accno);
		return accountRepository.findById(accno).orElseThrow(() -> new RuntimeException("Account Not Found"));

	}

	//---------------------------------------SEARCH BY EMAIL -----------------------------------------------------
	@Override
	public Account getByEmail(String email) {
		adv.validEmail(email);
		return accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email Not Found"));

	}

	//---------------------------------------SEARCH BY MOBILE NUMBER -----------------------------------------------------
	@Override
	public Account getByMobileNumber(String mob) {
		adv.validMobileNumber(mob);
		return accountRepository.findByMob(mob).orElseThrow(() -> new RuntimeException("Mobile Number Not Found"));
	}

	
	//---------------------------------------DISPLAY BALANCE-----------------------------------------------------
	@Override
	public BalanceDTO getBalance(Long accno) {

		
		Account account = accountRepository.findById(accno).orElseThrow(() -> new AccountNotFoundException("Account Not Found with account Number :"
				+ accno));
		return AccountBalanceMapper.toBalanceDTO(account);

	}

	//---------------------------------------UPDATE ACCOUNTS-----------------------------------------------------
	@Override
	public UpdateAccountDTO update(Long accno, UpdateAccountDTO updateAccountDTO) {

	    // 1️⃣ Fetch existing account
	    Account existingAccount = this.getByAccountNumber(accno);

	    // 2️⃣ Update only non-null fields
	    if (updateAccountDTO.getName() != null) {
	        adv.validName(updateAccountDTO.getName());
	        existingAccount.setName(updateAccountDTO.getName());
	    }

	    if (updateAccountDTO.getEmail() != null) {
	        adv.validEmail(updateAccountDTO.getEmail());
	        existingAccount.setEmail(updateAccountDTO.getEmail());
	    }

	    if (updateAccountDTO.getMob() != null) {
	        adv.validMobileNumber(updateAccountDTO.getMob());
	        existingAccount.setMob(updateAccountDTO.getMob());
	    }

	    if (updateAccountDTO.getAddress() != null) {
	        existingAccount.setAddress(updateAccountDTO.getAddress());
	    }

	    // 3️⃣ Save updated account
	    Account savedAccount = accountRepository.save(existingAccount);

	    // 4️⃣ Map saved account to UpdateAccountDTO
	    UpdateAccountDTO updatedDTO = new UpdateAccountDTO();
	    updatedDTO.setName(savedAccount.getName());
	    updatedDTO.setEmail(savedAccount.getEmail());
	    updatedDTO.setMob(savedAccount.getMob());
	    updatedDTO.setAddress(savedAccount.getAddress());

	    return updatedDTO;
	}
	
	//---------------------------------------WITHDRAW AMOUNT-----------------------------------------------------
	@Transactional
	@Override
	public BalanceDTO withdrawAmount(Long accno, Double amount) {

		// Search Account
		Account account = accountRepository.findById(accno).orElseThrow(() -> new AccountNotFoundException("Account Not Found with account Number :"
				+ accno));
		adv.validAmount(amount);

		if (account instanceof SavingAccount savingAccount) {

			// Min Balance Checking
			if (account.getBalance() - amount < savingAccount.getMIN_BALANCE()) {
				throw new InvalidAmount("You should have to maintain minimum balance!! You can only withdrow"
						+ (account.getBalance() - savingAccount.getMIN_BALANCE()));
			}

			// Withdraw Limit Checking
			if (amount > savingAccount.getWithdrawLimit()) {
				throw new InvalidAmount(
						"You cannot withdraw more than" + savingAccount.getWithdrawLimit() + "at a time");
			}

		} else if (account instanceof CurrentAccount currentAccount) {

			if (account.getBalance() - amount < currentAccount.getMIN_BALANCE())
				throw new InvalidAmount("You should have to maintain minimum balance!! You can only withdrow"
						+ (account.getBalance() - currentAccount.getMIN_BALANCE()));
		}

		account.setBalance(account.getBalance() - amount);

		return AccountBalanceMapper.toBalanceDTO(account);

	}

	
	//---------------------------------------DEPOSITE AMOUNT-----------------------------------------------------
	@Transactional
	// Manage DB transaction: commit if successful, rollback on error

	@Override
	public BalanceDTO depositAmount(Long accno, Double amount) {

		adv.validAmount(amount);

		// Search Account
		Account account = this.getByAccountNumber(accno);

		// Add balance in Account
		account.setBalance(account.getBalance() + amount);
		return AccountBalanceMapper.toBalanceDTO(account);
	}

}
