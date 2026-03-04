package com.bank.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.models.User;
import com.bank.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/display-all-employees")
	public ResponseEntity<List<User>> getAllEmployees(){
		List<User> users = adminService.getAllUsers();
		return ResponseEntity.ok(users);
	}
	
	@DeleteMapping("/delete-employee/{userId}")
	public void delete(Long userId) {
		adminService.delete(userId);
	}
	

	
}
