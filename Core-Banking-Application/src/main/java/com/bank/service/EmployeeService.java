package com.bank.service;

import java.util.List;

import com.bank.models.User;

public interface EmployeeService {
//
	void registerUser(User user);
	String login(String email, String password);
	

}
