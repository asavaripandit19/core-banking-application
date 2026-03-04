package com.bank.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class AccountTypeConfig {

	@Id
	@Enumerated(EnumType.STRING)
	private AccountType accountType; 
	
	private Double MIN_BALANCE;
	private Double WITHDRAW_LIMIT;
	

}

