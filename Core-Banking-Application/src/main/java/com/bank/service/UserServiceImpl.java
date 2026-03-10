package com.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.exception.AccountDetailsValidation;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bank.models.Role;
import com.bank.models.User;
import com.bank.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService,UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountDetailsValidation adv;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void registerUser(User user) {

		
		adv.validEmail(user.getEmail());

		String regex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,}$";

		if (!user.getPassword().matches(regex)) {
			throw new RuntimeException(
					"Password must be minimum 6 characters, contain at least one capital letter and one special symbol.");
		}

		Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser.isPresent())
			throw new RuntimeException("Email alredy exits!");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.EMPLOYEE);
		userRepository.save(user);
	}

	@Override
	public String login(String email, String password) {
		Optional<User> temp = userRepository.findByEmail(email);
		if (temp == null) {
			return "User Not Found!";
		}
		User user = temp.get();   

	    if (passwordEncoder.matches(password, user.getPassword())) {
	        return "User login Successful!!";
	    }
		return "Invalid Password!!";
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//fetch user present in the db
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

				
		
		
		//inject db user in spring security User object
		return org.springframework.security.core.userdetails.User.
				builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.roles(user.getRole().name())
				.build();
	}
	
	
	
}
