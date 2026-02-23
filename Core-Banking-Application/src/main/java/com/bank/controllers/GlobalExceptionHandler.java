package com.bank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidAadharNumber;
import com.bank.exception.InvalidAccountNumber;
import com.bank.exception.InvalidAmount;
import com.bank.exception.InvalidEmail;
import com.bank.exception.InvalidMobileNumber;
import com.bank.exception.InvalidName;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InvalidAadharNumber.class)
	public ResponseEntity<?> invalidAadharNumber(InvalidAadharNumber e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(InvalidAccountNumber.class)
	public ResponseEntity<?> invalidAccountNumber(InvalidAccountNumber e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(InvalidAmount.class)
	public ResponseEntity<?> invalidAmount(InvalidAmount e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(InvalidEmail.class)
	public ResponseEntity<?> invalidEmail(InvalidEmail e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(InvalidMobileNumber.class)
	public ResponseEntity<?> invalidMobileNumber(InvalidMobileNumber e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(InvalidName.class)
	public ResponseEntity<?> invalidName(InvalidName e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}
	
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<?> accountNotFoundException(AccountNotFoundException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

}
