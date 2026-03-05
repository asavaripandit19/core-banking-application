package com.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.models.AccountType;
import com.bank.models.AccountTypeConfig;
import com.bank.models.User;
import com.bank.repository.AccountTypeConfigRepository;
import com.bank.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountTypeConfigRepository accConfigRepository;
	
	
	@Override
	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}


	@Override
	public void delete(Long userId) {
		  if(userId == null){
		        throw new RuntimeException("User ID cannot be null");
		    }
		userRepository.deleteById(userId);
		
	}


	@Override
	public void updateMinBalance(Double amount, AccountType type) {
		AccountTypeConfig config = accConfigRepository.findById(type).orElseThrow(() -> new RuntimeException("Account type not found!"));
		config.setMIN_BALANCE(amount);
		accConfigRepository.save(config);
	}


	@Override
	public void updateWithdrawBalance(Double amount, AccountType type) {
		AccountTypeConfig config = accConfigRepository.findById(type).orElseThrow(() -> new RuntimeException("Account type not found!"));
		
		if(type==AccountType.CURRENT)
			throw new RuntimeException("You cannot set withdraw limit to Current account!!");
		
		config.setWITHDRAW_LIMIT(amount);
		accConfigRepository.save(config);
		
	}
}
