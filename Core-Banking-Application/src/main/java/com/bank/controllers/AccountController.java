package com.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.AccountDisplayDTO;
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
		accountService.createAccount(currentAccount);;
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
	
}
