package com.bank.service;

import java.util.List;

import com.bank.models.User;

public interface AdminService {

	List<User> getAllUsers();
	void delete(Long userId);
	
}
