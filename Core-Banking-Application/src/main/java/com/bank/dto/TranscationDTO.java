package com.bank.dto;

import java.sql.SQLNonTransientConnectionException;
import java.time.LocalDate;
import java.time.LocalTime;

import com.bank.models.Transaction;
import com.bank.models.TransactionType;

import lombok.Data;

@Data
public class TranscationDTO {

	private Long transcationId;
	private LocalDate transactionDate;
	private LocalTime transactionTime;
	private Double amount;
	private TransactionType transactionType;
	
	public static TranscationDTO toSafeTranscationDto(Transaction transaction) {
		TranscationDTO dto = new TranscationDTO();
		
		dto.setTranscationId(transaction.getTranscationId());
		dto.setAmount(transaction.getAmount());
		dto.setTransactionDate(transaction.getTransactionDate());
		dto.setTransactionTime(transaction.getTransactionTime());
		dto.setTransactionType(transaction.getTransactionType());
		return dto;
	}
}
