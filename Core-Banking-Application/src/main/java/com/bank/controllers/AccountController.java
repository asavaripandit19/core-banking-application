package com.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.AccountDisplayDTO;
import com.bank.dto.BalanceDTO;
import com.bank.dto.UpdateAccountDTO;
import com.bank.models.Account;
import com.bank.models.CurrentAccount;
import com.bank.models.SavingAccount;
import com.bank.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

	@Autowired
	private AccountService accountService;

	// ------------------ Create Saving Accounts ------------------
	@PostMapping("create-saving-account")
	public void createSavingAccount(@RequestBody SavingAccount savingAccount) {
		accountService.createAccount(savingAccount);
	}

	// ------------------ Create Current Account --------------------
	@PostMapping("create-current-account")
	public void createCurrentAccount(@RequestBody CurrentAccount currentAccount) {
		accountService.createAccount(currentAccount);
		;
	}

	// ------------------ Display Only Current Accounts ------------------
	@GetMapping("display-current-accounts")
	public List<AccountDisplayDTO> displayCurrentlAccount() {
		return accountService.getCurrentAccounts();
	}

	// ------------------ Display Only Saving Accounts ------------------
	@GetMapping("display-saving-accounts")
	public List<AccountDisplayDTO> displaySvaingAccounts() {
		return accountService.getSvaingAccounts();
	}

	// ------------------ Display All Accounts --------------------------
	@GetMapping("display-all-accounts")
	public List<AccountDisplayDTO> displayAllAccounts() {
		return accountService.getAllAccounts();
	}

	// ------------------ Close Account --------------------------
	@DeleteMapping("close-account/{accNo}")
	public String closeAccount(@PathVariable Long accNo) {
		return accountService.closeAccount(accNo);
	}

	// ------------------ Check balance --------------------------
	@GetMapping("check-balance/{accNo}")
	public ResponseEntity<BalanceDTO> checkBalance(@PathVariable Long accNo) {
		BalanceDTO balanceDTO = accountService.getBalance(accNo);
		return ResponseEntity.ok(balanceDTO);
	}

	// ------------------ Withdraw Amount --------------------------
	@PutMapping("/withdraw-amount/{accno}/{amount}")
	public ResponseEntity<BalanceDTO> withdrawAmount(@PathVariable Long accno, @PathVariable Double amount) {
		BalanceDTO balanceDTO = accountService.withdrawAmount(accno, amount);
		return ResponseEntity.ok(balanceDTO);
	}

	// ------------------ Deposite Amount --------------------------
	@PutMapping("/deposit-amount/{accno}/{amount}")
	public ResponseEntity<BalanceDTO> depositeAmount(@PathVariable Long accno, @PathVariable Double amount) {
		BalanceDTO balanceDTO = accountService.depositAmount(accno, amount);
		return ResponseEntity.ok(balanceDTO);
	}

	// ------------------ Update Account --------------------------
	@PutMapping("/update-account/{accno}")
	public ResponseEntity<UpdateAccountDTO> updateAccount(@PathVariable Long accno, @RequestBody UpdateAccountDTO updateAccountDTO) {
		UpdateAccountDTO updatedDTO = accountService.update(accno, updateAccountDTO);
		return ResponseEntity.ok(updatedDTO);
	}
	
	@GetMapping("/search-by-account-number/{accno}")
	public ResponseEntity<Account> getAccount(@PathVariable Long accno) {
		return ResponseEntity.ok(accountService.getByAccountNumber(accno));
	}
	
	@GetMapping("/search-by-email/{email}")
	public ResponseEntity<Account> getByEmail(@PathVariable String email) {
		return ResponseEntity.ok(accountService.getByEmail(email));
	}
	
	@GetMapping("/search-by-mobile-number/{mob}")
	public ResponseEntity<Account> getByMobileNumber(@PathVariable String mob) {
		return ResponseEntity.ok(accountService.getByMobileNumber(mob));
	}
}
