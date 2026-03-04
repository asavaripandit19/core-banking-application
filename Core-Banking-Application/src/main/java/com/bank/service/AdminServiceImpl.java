package com.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.models.User;
import com.bank.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public List<User> getAllUsers() {
		
		return userRepository.findAll();
	}


	@Override
	public void delete(Long userId) {
		userRepository.deleteById(userId);
		
	}
}
