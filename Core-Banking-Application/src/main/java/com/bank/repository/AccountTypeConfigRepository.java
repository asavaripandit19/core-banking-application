package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.models.AccountType;
import com.bank.models.AccountTypeConfig;

public interface AccountTypeConfigRepository extends JpaRepository<AccountTypeConfig, AccountType> {

}
