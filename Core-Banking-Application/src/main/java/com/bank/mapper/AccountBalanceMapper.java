package com.bank.mapper;

import com.bank.dto.BalanceDTO;
import com.bank.models.Account;

public class AccountBalanceMapper {

	public static BalanceDTO toBalanceDTO(Account account) {
		return new BalanceDTO(account.getName(), account.getBalance());
	}
}
