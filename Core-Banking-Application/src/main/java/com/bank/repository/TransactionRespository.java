package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.models.Transaction;

@Repository
public interface TransactionRespository extends JpaRepository<Transaction, Long>{

}
