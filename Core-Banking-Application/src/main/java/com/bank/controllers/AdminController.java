package com.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.models.AccountType;
import com.bank.models.User;
import com.bank.service.AdminService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/display-all-employees")
	public ResponseEntity<List<User>> getAllEmployees(){
		List<User> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}
	
	@DeleteMapping("/delete-employee/{userId}")
	public void delete(@PathVariable Long userId) {
		adminService.delete(userId);
	}
	
	@PutMapping("update-minbalance/{type}/{amount}")
	public void updateMinBalance(@PathVariable  Double amount, @PathVariable AccountType type) {
		
		adminService.updateMinBalance(amount, type) ;
		
	}

	@PutMapping("update-withdrawlimit/{type}/{amount}")
	public void updateWithdrawBalance(@PathVariable  Double amount, @PathVariable AccountType type) {
		
		adminService.updateWithdrawBalance(amount, type) ;
		
	}
}
