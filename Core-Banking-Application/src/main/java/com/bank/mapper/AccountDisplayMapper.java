package com.bank.mapper;

import com.bank.dto.AccountDisplayDTO;
import com.bank.models.Account;
import com.bank.models.SavingAccount;

public class AccountDisplayMapper {

	public static AccountDisplayDTO toSafeDto(Account account) {
		AccountDisplayDTO dto = new AccountDisplayDTO();
		
		//hide accno 4 digits
		String accNo = account.getAccNo().toString();
		if(accNo.length()> 4) {
			dto.setAccNo("********" + accNo.substring(accNo.length() -4));
		}
		else {
			dto.setAccNo(accNo);
		}
		
		// set name
		dto.setName(account.getName());
		
		//set email
		dto.setEmail(account.getEmail());
		
		//set address
		dto.setAddress(account.getAddress());
		
		//set date
		dto.setCreatedDate(account.getDate());
		
		//set account type
		if(account instanceof SavingAccount)
			dto.setAcctype("SAVING");
		else
			dto.setAcctype("CURRENT");
		
		
		return dto;
		
		
	}
}
