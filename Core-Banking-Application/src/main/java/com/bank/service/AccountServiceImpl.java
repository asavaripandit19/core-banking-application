package com.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.dto.AccountDisplayDTO;
import com.bank.dto.BalanceDTO;
import com.bank.dto.TranscationDTO;
import com.bank.dto.UpdateAccountDTO;
import com.bank.email.EmailService;
import com.bank.exception.AccountDetailsValidation;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidAmount;
import com.bank.mapper.AccountBalanceMapper;
import com.bank.mapper.AccountDisplayMapper;
import com.bank.models.Account;
import com.bank.models.CurrentAccount;
import com.bank.models.SavingAccount;
import com.bank.models.Transaction;
import com.bank.models.TransactionType;
import com.bank.repository.AccountRepository;
import com.bank.repository.CurrentAccountRepository;
import com.bank.repository.SavingAccountRepository;
import com.bank.repository.TransactionRespository;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class AccountServiceImpl implements AccountService {

	//aefed
	@Autowired
	private EmailService emailService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountDetailsValidation adv;

	@Autowired
	private SavingAccountRepository savingAccountRepository;

	@Autowired
	private CurrentAccountRepository currentAccountRepository;

	@Autowired
	private TransactionRespository transactionRespository;

	// ---------------------------------------CREATE
	// ACCOUNT-----------------------------------------------------
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

		String subject = "Welcome to PaySpring Bank! 🎉";

		String message = "<!DOCTYPE html>" + "<html>"
				+ "<body style='margin:0;padding:0;font-family:Segoe UI,Arial,sans-serif;background-color:#f4f6f9;'>" +

				"<table align='center' width='600' cellpadding='0' cellspacing='0' "
				+ "style='background:#ffffff;margin-top:40px;border-radius:12px;overflow:hidden;"
				+ "box-shadow:0 8px 20px rgba(0,0,0,0.08);'>" +

				// Header
				"<tr>"
				+ "<td style='background:linear-gradient(90deg,#0D47A1,#1976D2);padding:25px;text-align:center;color:white;'>"
				+ "<h2 style='margin:0;'>💳 PaySpring Bank</h2>"
				+ "<p style='margin:5px 0 0;font-size:13px;opacity:0.9;'>Secure. Smart. Reliable Banking.</p>" + "</td>"
				+ "</tr>" +

				// Body
				"<tr>" + "<td style='padding:35px;color:#333;'>"
				+ "<h3 style='margin-top:0;color:#2E7D32;'>Welcome, <strong>" + account.getName() + "</strong>! 🎉</h3>"
				+ "<p style='font-size:15px;color:#555;line-height:1.6;'>"
				+ "We are thrilled to have you on board with PaySpring Bank. "
				+ "We look forward to providing you with secure and convenient banking services." + "</p>" + "</td>"
				+ "</tr>" +

				// Footer
				"<tr>" + "<td style='background:#f4f6f9;text-align:center;padding:15px;font-size:12px;color:#777;'>"
				+ "© 2026 PaySpring Bank. All rights reserved.<br>" + "This is an automated email. Please do not reply."
				+ "</td>" + "</tr>" +

				"</table>" + "</body>" + "</html>";

		emailService.sendMail(account.getEmail(), subject, message);
		accountRepository.save(account);
	}

	// ---------------------------------------DISPLAY
	// ACCOUNTS-----------------------------------------------------
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

	@Override
	public List<TranscationDTO> getTransactionsHistory(Long accno) {

		Account temp = this.getByAccountNumber(accno);
		List<Transaction> transcations = transactionRespository.findByAccNo(accno);
		if (transcations.isEmpty())
			throw new AccountNotFoundException("No transcation found for account : " + accno);

		return transcations.stream().map(TranscationDTO::toSafeTranscationDto).toList();

	}

	// ---------------------------------------CLOSE
	// ACCOUNTS-----------------------------------------------------
	@Transactional
	@Override
	public String closeAccount(Long accno) {

		Account account = this.getByAccountNumber(accno);

//		if(temp.getBalance() < 0) {
//			temp.setBalance(0.0);
//			 accountRepository.save(temp);
//		}

		accountRepository.deleteById(accno);
		String subject = "Close Account";
		String message = "<!DOCTYPE html>" + "<html>"
				+ "<body style='margin:0;padding:0;font-family:Segoe UI,Arial,sans-serif;background-color:#f4f6f9;'>" +

				"<table align='center' width='600' cellpadding='0' cellspacing='0' "
				+ "style='background:#ffffff;margin-top:40px;border-radius:12px;overflow:hidden;"
				+ "box-shadow:0 8px 20px rgba(0,0,0,0.08);'>" +

				// Header
				"<tr>"
				+ "<td style='background:linear-gradient(90deg,#B71C1C,#E53935);padding:25px;text-align:center;color:white;'>"
				+ "<h2 style='margin:0;'>💳 PaySpring Bank</h2>"
				+ "<p style='margin:5px 0 0;font-size:13px;opacity:0.9;'>Secure. Smart. Reliable Banking.</p>" + "</td>"
				+ "</tr>" +

				// Body
				"<tr>" + "<td style='padding:35px;color:#333;'>"
				+ "<h3 style='margin-top:0;color:#C62828;'>Account Closure Request Failed ❌</h3>"
				+ "<p style='font-size:15px;'>Dear <strong>" + account.getName() + "</strong>,</p>" +

				"<p style='font-size:15px;color:#555;line-height:1.6;'>"
				+ "We regret to inform you that your account closure request could not be processed at this time."
				+ "</p>" +

				// Balance Warning Box
				"<div style='background:#fff3f3;padding:20px;border-radius:8px;margin:20px 0;'>"
				+ "<p style='margin:8px 0;font-size:16px;'><strong>Remaining Balance:</strong> "
				+ "<span style='color:#C62828;font-weight:bold;'>₹" + account.getBalance() + "</span></p>" +

				"<p style='margin:8px 0;font-size:14px;'>"
				+ "Kindly withdraw the remaining balance before requesting account closure." + "</p>" + "</div>" +

				"<p style='font-size:14px;color:#555;'>"
				+ "For security and banking regulations, accounts with a remaining balance cannot be closed." + "</p>" +

				"<p style='margin-top:25px;font-size:14px;color:#555;'>"
				+ "Thank you for banking with <strong>PaySpring Bank</strong>.<br>"
				+ "We are always here to assist you." + "</p>" +

				"</td>" + "</tr>" +

				// Footer
				"<tr>" + "<td style='background:#f4f6f9;text-align:center;padding:15px;font-size:12px;color:#777;'>"
				+ "© 2026 PaySpring Bank. All rights reserved.<br>" + "This is an automated email. Please do not reply."
				+ "</td>" + "</tr>" +

				"</table>" + "</body>" + "</html>";
		emailService.sendMail(account.getEmail(), subject, message);
		return "Account Close Successfully!!";
	}

	// ---------------------------------------SERACH
	// ACCOUNT-----------------------------------------------------
	@Override
	public Account getByAccountNumber(Long accno) {
		adv.validAccountNumber(accno);
		return accountRepository.findById(accno).orElseThrow(() -> new AccountNotFoundException("Account Not Found"));

	}

	// ---------------------------------------SEARCH BY EMAIL
	// -----------------------------------------------------
	@Override
	public Account getByEmail(String email) {
		adv.validEmail(email);
		return accountRepository.findByEmail(email).orElseThrow(() -> new AccountNotFoundException("Email Not Found"));

	}

	// ---------------------------------------SEARCH BY MOBILE NUMBER
	// -----------------------------------------------------
	@Override
	public Account getByMobileNumber(String mob) {
		adv.validMobileNumber(mob);
		return accountRepository.findByMob(mob)
				.orElseThrow(() -> new AccountNotFoundException("Mobile Number Not Found"));
	}

	// ---------------------------------------DISPLAY
	// BALANCE-----------------------------------------------------
	@Override
	public BalanceDTO getBalance(Long accno) {

		Account account = this.getByAccountNumber(accno);
		String subject = "Check Balance";
		String message = "<!DOCTYPE html>" + "<html>"
				+ "<body style='margin:0;padding:0;font-family:Segoe UI,Arial,sans-serif;background-color:#f4f6f9;'>" +

				"<table align='center' width='600' cellpadding='0' cellspacing='0' "
				+ "style='background:#ffffff;margin-top:40px;border-radius:12px;overflow:hidden;"
				+ "box-shadow:0 8px 20px rgba(0,0,0,0.08);'>" +

				// Header
				"<tr>"
				+ "<td style='background:linear-gradient(90deg,#004D40,#009688);padding:25px;text-align:center;color:white;'>"
				+ "<h2 style='margin:0;'>💳 PaySpring Bank</h2>"
				+ "<p style='margin:5px 0 0;font-size:13px;opacity:0.9;'>Secure. Smart. Reliable Banking.</p>" + "</td>"
				+ "</tr>" +

				// Body
				"<tr>" + "<td style='padding:35px;color:#333;'>"
				+ "<h3 style='margin-top:0;color:#00796B;'>Account Balance Information 💰</h3>"
				+ "<p style='font-size:15px;'>Dear <strong>" + account.getName() + "</strong>,</p>" +

				"<p style='font-size:15px;color:#555;line-height:1.6;'>"
				+ "Here is your current account balance details as requested." + "</p>" +

				// Balance Box
				"<div style='background:#e0f2f1;padding:20px;border-radius:8px;margin:20px 0;text-align:center;'>"
				+ "<p style='margin:8px 0;font-size:18px;'><strong>Available Balance</strong></p>"
				+ "<p style='margin:8px 0;font-size:26px;font-weight:bold;color:#004D40;'>₹" + account.getBalance()
				+ "</p>" +

				"<p style='margin:8px 0;font-size:14px;'><strong>Date:</strong> " + java.time.LocalDate.now() + "</p>"
				+ "</div>" +

				"<p style='font-size:14px;color:#555;'>"
				+ "For security reasons, please do not share your account details with anyone." + "</p>" +

				"<p style='margin-top:25px;font-size:14px;color:#555;'>"
				+ "Thank you for banking with <strong>PaySpring Bank</strong>.<br>"
				+ "We are always here to serve you better." + "</p>" +

				"</td>" + "</tr>" +

				// Footer
				"<tr>" + "<td style='background:#f4f6f9;text-align:center;padding:15px;font-size:12px;color:#777;'>"
				+ "© 2026 PaySpring Bank. All rights reserved.<br>" + "This is an automated email. Please do not reply."
				+ "</td>" + "</tr>" +

				"</table>" + "</body>" + "</html>";
		emailService.sendMail(account.getEmail(), subject, message);
		return AccountBalanceMapper.toBalanceDTO(account);

	}

	// ---------------------------------------UPDATE
	// ACCOUNTS-----------------------------------------------------
	@Override
	public UpdateAccountDTO update(Long accno, UpdateAccountDTO updateAccountDTO) {

		Account existingAccount = this.getByAccountNumber(accno);

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

		Account savedAccount = accountRepository.save(existingAccount);

		UpdateAccountDTO updatedDTO = new UpdateAccountDTO();
		updatedDTO.setName(savedAccount.getName());
		updatedDTO.setEmail(savedAccount.getEmail());
		updatedDTO.setMob(savedAccount.getMob());
		updatedDTO.setAddress(savedAccount.getAddress());

		String subject = "Update Details Successfully";
		String message = "<!DOCTYPE html>" + "<html>"
				+ "<body style='margin:0;padding:0;font-family:Segoe UI,Arial,sans-serif;background-color:#f4f6f9;'>" +

				"<table align='center' width='600' cellpadding='0' cellspacing='0' "
				+ "style='background:#ffffff;margin-top:40px;border-radius:12px;overflow:hidden;"
				+ "box-shadow:0 8px 20px rgba(0,0,0,0.08);'>" +

				// Header
				"<tr>"
				+ "<td style='background:linear-gradient(90deg,#1565C0,#1E88E5);padding:25px;text-align:center;color:white;'>"
				+ "<h2 style='margin:0;'>💳 PaySpring Bank</h2>"
				+ "<p style='margin:5px 0 0;font-size:13px;opacity:0.9;'>Secure. Smart. Reliable Banking.</p>" + "</td>"
				+ "</tr>" +

				// Body
				"<tr>" + "<td style='padding:35px;color:#333;'>"
				+ "<h3 style='margin-top:0;color:#2E7D32;'>Account Details Updated ✅</h3>"
				+ "<p style='font-size:15px;'>Dear <strong>" + savedAccount.getName() + "</strong>,</p>" +

				"<p style='font-size:15px;color:#555;line-height:1.6;'>"
				+ "Your account details have been successfully updated. Here are the latest details:" + "</p>" +

				// Details Box
				"<div style='background:#f1f5ff;padding:20px;border-radius:8px;margin:20px 0;'>"
				+ "<p style='margin:8px 0;font-size:16px;'><strong>Name:</strong> " + savedAccount.getName() + "</p>"
				+ "<p style='margin:8px 0;font-size:16px;'><strong>Email:</strong> " + savedAccount.getEmail() + "</p>"
				+ "<p style='margin:8px 0;font-size:16px;'><strong>Mobile:</strong> " + savedAccount.getMob() + "</p>"
				+ "<p style='margin:8px 0;font-size:16px;'><strong>Address:</strong> " + savedAccount.getAddress()
				+ "</p>" + "</div>" +

				"<p style='font-size:14px;color:#555;'>"
				+ "If you did not request this update, please contact our 24/7 customer support immediately." + "</p>" +

				"<p style='margin-top:25px;font-size:14px;color:#555;'>"
				+ "Thank you for banking with <strong>PaySpring Bank</strong>.<br>"
				+ "We are always here to serve you better." + "</p>" +

				"</td>" + "</tr>" +

				// Footer
				"<tr>" + "<td style='background:#f4f6f9;text-align:center;padding:15px;font-size:12px;color:#777;'>"
				+ "© 2026 PaySpring Bank. All rights reserved.<br>" + "This is an automated email. Please do not reply."
				+ "</td>" + "</tr>" +

				"</table>" + "</body>" + "</html>";
		emailService.sendMail(existingAccount.getEmail(), subject, message);

		return updatedDTO;
	}

	// ---------------------------------------WITHDRAW
	// AMOUNT-----------------------------------------------------
	@Transactional
	@Override
	public BalanceDTO withdrawAmount(Long accno, Double amount) {

		// Search Account
		Account account = this.getByAccountNumber(accno);
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
		setTransaction(accno, amount, TransactionType.DEBIT);
		String subject = "Withdraw Successful";
		String message = "<!DOCTYPE html>" + "<html>"
				+ "<body style='margin:0;padding:0;font-family:Segoe UI,Arial,sans-serif;background-color:#f4f6f9;'>" +

				"<table align='center' width='600' cellpadding='0' cellspacing='0' "
				+ "style='background:#ffffff;margin-top:40px;border-radius:12px;overflow:hidden;"
				+ "box-shadow:0 8px 20px rgba(0,0,0,0.08);'>" +

				// Header
				"<tr>"
				+ "<td style='background:linear-gradient(90deg,#B71C1C,#E53935);padding:25px;text-align:center;color:white;'>"
				+ "<h2 style='margin:0;'>💳 PaySpring Bank</h2>"
				+ "<p style='margin:5px 0 0;font-size:13px;opacity:0.9;'>Secure. Smart. Reliable Banking.</p>" + "</td>"
				+ "</tr>" +

				// Body
				"<tr>" + "<td style='padding:35px;color:#333;'>"
				+ "<h3 style='margin-top:0;color:#C62828;'>Withdrawal Successful ✅</h3>"
				+ "<p style='font-size:15px;'>Dear <strong>" + account.getName() + "</strong>,</p>" +

				"<p style='font-size:15px;color:#555;line-height:1.6;'>"
				+ "This is to inform you that a withdrawal has been successfully processed from your account." + "</p>"
				+

				// Transaction Box
				"<div style='background:#fff3f3;padding:20px;border-radius:8px;margin:20px 0;'>"
				+ "<p style='margin:8px 0;font-size:16px;'><strong>Withdrawn Amount:</strong> "
				+ "<span style='color:#C62828;font-weight:bold;'>₹" + amount + "</span></p>" +

				"<p style='margin:8px 0;font-size:16px;'><strong>Remaining Balance:</strong> "
				+ "<span style='color:#0D47A1;font-weight:bold;'>₹" + account.getBalance() + "</span></p>" +

				"<p style='margin:8px 0;font-size:14px;'><strong>Date:</strong> " + java.time.LocalDate.now() + "</p>"
				+ "</div>" +

				"<p style='font-size:14px;color:#555;'>"
				+ "If you did not authorize this transaction, please contact our 24/7 customer support immediately."
				+ "</p>" +

				"<p style='margin-top:25px;font-size:14px;color:#555;'>"
				+ "Thank you for banking with <strong>PaySpring Bank</strong>.<br>"
				+ "We value your trust and confidence in us." + "</p>" +

				"</td>" + "</tr>" +

				// Footer
				"<tr>" + "<td style='background:#f4f6f9;text-align:center;padding:15px;font-size:12px;color:#777;'>"
				+ "© 2026 PaySpring Bank. All rights reserved.<br>" + "This is an automated email. Please do not reply."
				+ "</td>" + "</tr>" +

				"</table>" + "</body>" + "</html>";
		emailService.sendMail(account.getEmail(), subject, message);

		return AccountBalanceMapper.toBalanceDTO(account);

	}

	// ---------------------------------------DEPOSITE
	// AMOUNT-----------------------------------------------------
	@Transactional
	// Manage DB transaction: commit if successful, rollback on error

	@Override
	public BalanceDTO depositAmount(Long accno, Double amount) {

		// 1️⃣ Validate amount
		adv.validAmount(amount);

		// 2️⃣ Get account
		Account account = this.getByAccountNumber(accno);

		account.setBalance(account.getBalance() + amount);

		setTransaction(accno, amount, TransactionType.CREDIT);

		String subject = "Deposit Successful";
		String message = "<!DOCTYPE html>" + "<html>"
				+ "<body style='margin:0;padding:0;font-family:Segoe UI,Arial,sans-serif;background-color:#f4f6f9;'>" +

				"<table align='center' width='600' cellpadding='0' cellspacing='0' "
				+ "style='background:#ffffff;margin-top:40px;border-radius:12px;overflow:hidden;"
				+ "box-shadow:0 8px 20px rgba(0,0,0,0.08);'>" +

				// Header
				"<tr>"
				+ "<td style='background:linear-gradient(90deg,#0D47A1,#1976D2);padding:25px;text-align:center;color:white;'>"
				+ "<h2 style='margin:0;'>💳 PaySpring Bank</h2>"
				+ "<p style='margin:5px 0 0;font-size:13px;opacity:0.9;'>Secure. Smart. Reliable Banking.</p>" + "</td>"
				+ "</tr>" +

				// Body
				"<tr>" + "<td style='padding:35px;color:#333;'>" + "<h3 style='margin-top:0;'>Deposit Successful ✅</h3>"
				+ "<p style='font-size:15px;'>Dear <strong>" + account.getName() + "</strong>,</p>" +

				"<p style='font-size:15px;color:#555;line-height:1.6;'>"
				+ "We are pleased to inform you that your deposit has been successfully credited to your account."
				+ "</p>" +

				// Transaction Box
				"<div style='background:#f1f5ff;padding:20px;border-radius:8px;margin:20px 0;'>"
				+ "<p style='margin:8px 0;font-size:16px;'><strong>Deposited Amount:</strong> "
				+ "<span style='color:#2E7D32;font-weight:bold;'>₹" + amount + "</span></p>" +

				"<p style='margin:8px 0;font-size:16px;'><strong>Available Balance:</strong> "
				+ "<span style='color:#0D47A1;font-weight:bold;'>₹" + account.getBalance() + "</span></p>" +

				"<p style='margin:8px 0;font-size:14px;'><strong>Date:</strong> " + java.time.LocalDate.now() + "</p>"
				+ "</div>" +

				"<p style='font-size:14px;color:#555;'>"
				+ "If you did not authorize this transaction, please contact our 24/7 customer support immediately."
				+ "</p>" +

				"<p style='margin-top:25px;font-size:14px;color:#555;'>"
				+ "Thank you for choosing <strong>PaySpring Bank</strong>.<br>" + "We appreciate your trust in us."
				+ "</p>" +

				"</td>" + "</tr>" +
				// Footer
				// aefs
				"<tr>" + "<td style='background:#f4f6f9;text-align:center;padding:15px;font-size:12px;color:#777;'>"
				+ "© 2026 PaySpring Bank. All rights reserved.<br>" + "This is an automated email. Please do not reply."
				+ "</td>" + "</tr>" +

				"</table>" + "</body>" + "</html>";
		emailService.sendMail(account.getEmail(), subject, message);
		//aefea
		return AccountBalanceMapper.toBalanceDTO(account);
	}

	@Override
	public void setTransaction(Long accNo, Double amount, TransactionType transactionType) {

		transactionRespository.save(new Transaction(accNo, amount, transactionType));
	}

}
