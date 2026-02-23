package com.bank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByEmail(String email);

	Optional<Account> findByMob(String mob);

}
