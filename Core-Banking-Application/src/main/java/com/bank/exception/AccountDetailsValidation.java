package com.bank.exception;

import org.springframework.stereotype.Component;

@Component
public class AccountDetailsValidation extends RuntimeException {

	public void validAccountNumber(Long accno) {
		if (accno == null)
			throw new InvalidAccountNumber("Account number cannot be Null");

		if (accno <= 0)
			throw new InvalidAccountNumber("Account number cannot be negative");

	}

	public void validEmail(String email) {

		if (email.trim().isEmpty()) {
			throw new InvalidEmail("Email is required");
		}

		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (!email.matches(emailRegex))
			throw new InvalidEmail("Invalid Emial formate");
	}

	public void validMobileNumber(String mob) {

		if (mob.trim().length() > 10 || mob.trim().length() < 10) {
			throw new InvalidMobileNumber("Mobile is required");
		}
		

		String mobileRegex = "^[6-9][0-9]{9}$";
		if (!mob.matches(mobileRegex))
			throw new InvalidMobileNumber("Invalid mobile Number");

	}
	
	public void validName(String name) {

	    if (name == null || name.trim().isEmpty()) {
	        throw new InvalidName("Name cannot be empty");
	    }

	    String nameRegex = "^[A-Za-z]+ [A-Za-z]+ [A-Za-z]+$";

	    if (!name.matches(nameRegex)) {
	        throw new InvalidName("Full name must contain exactly 3 words (First, Middle, Last) with alphabets only");
	    }
	}

	
	public void validAadharNumber(String aadharNo) {
		String aadharRegex = "^\\d{4}\\s?\\d{4}\\s?\\d{4}$";
		String aadhar = String.valueOf(aadharNo);
		
		if(!aadhar.matches(aadharRegex)) {
			throw new InvalidAadharNumber("Invalid Aadhar Number!");
		}
		
	}
	
	public void validAmount(Double amt) {
		if(amt == null) {
			throw new InvalidAmount("Enter Should have enter the amount");
		}
		
		if(amt< 0) {
			throw new InvalidAmount("Amount cannot be negative!!");
		}
		
		
		
	}
}
