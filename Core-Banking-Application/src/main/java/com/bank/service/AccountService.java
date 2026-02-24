package com.bank.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bank.dto.AccountDisplayDTO;
import com.bank.dto.BalanceDTO;
import com.bank.dto.UpdateAccountDTO;
import com.bank.models.Account;
import com.bank.models.SavingAccount;
import com.bank.models.TransactionType;


public interface AccountService {

	//create
	void createAccount(Account account);
	
	//display all account
	List<AccountDisplayDTO> getAllAccounts();
	
	//display saving accounts
	List<AccountDisplayDTO> getSvaingAccounts();
	
	//display current accounts
		List<AccountDisplayDTO> getCurrentAccounts();
	
	//update
	UpdateAccountDTO update(Long id, UpdateAccountDTO updateAccountDTO);
	
	//delete
	String closeAccount(Long accno);
	
	//Search
	Account getByAccountNumber(Long accno);
	Account getByEmail(String email);
	Account getByMobileNumber(String mob);
	
	//Check balance
	BalanceDTO getBalance(Long accno);
	
	//Withdraw amount
	BalanceDTO withdrawAmount(Long accno, Double amount);
	
	//deposite amount
	BalanceDTO depositAmount(Long accno, Double amount);
	
	void setTransaction(Long accNo, Double amount, TransactionType transactionType);
	
	
	
}
