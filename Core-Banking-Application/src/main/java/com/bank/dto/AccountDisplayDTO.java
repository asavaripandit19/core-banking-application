package com.bank.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AccountDisplayDTO {

	private String accNo;
	private String name;
	private String email;
	private String address;
	private LocalDate createdDate;
	private String Acctype;
	
}
