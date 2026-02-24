package com.bank.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="Pay_Spring_Transaction_Table")

public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long transcationId;
	private Long accNo;
	private LocalDate transactionDate;
	private LocalTime transactionTime;
	private Double amount;
	
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	
	@PrePersist
	public void ontransactionDone() {
		this.transactionDate=LocalDate.now();
		this.transactionTime=LocalTime.now();
	}
	
	public Transaction(Long accno, Double amt, TransactionType transactionType) {
		this.accNo=accno;
		this.amount=amt;
		this.transactionType=transactionType;
	}
	
	
}
