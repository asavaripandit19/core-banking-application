package com.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.exception.AccountDetailsValidation;
import com.bank.exception.UsernameNotFoundException;
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

		User existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser != null)
			throw new RuntimeException("Email alredy exits!");

		userRepository.save(user);
	}

	@Override
	public String login(String email, String password) {
		User temp = userRepository.findByEmail(email);
		if (temp == null) {
			return "User Not Found!";
		}
		if (password.equals(temp.getPassword())) {
			return "User login Successful!!";
		}
		return "Invalid Password!!";
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//fetch user present in the db
		User user = userRepository.findEmail(email).get();
				
		System.out.println("email is "+user.getEmail());
		
		//inject db user in spring security User object
		return org.springframework.security.core.userdetails.User.
				builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.roles(user.getRole().name())
				.build();
	}
	

}
