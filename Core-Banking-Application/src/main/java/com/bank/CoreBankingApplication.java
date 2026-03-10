package com.bank;

import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bank.models.Role;
import com.bank.models.User;
import com.bank.repository.UserRepository;

@SpringBootApplication
public class CoreBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreBankingApplication.class, args);
		System.out.println("Ok");
		
	}
	
	
	@Bean
	CommandLineRunner makeAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	    return args -> {

	        String email = "admin@gmail.com";

	        Optional<User> user = userRepository.findByEmail(email);

	        if(user.isEmpty()) {

	            User admin = new User();
	            admin.setUsername("Admin");
	            admin.setEmail("admin@gmail.com");
	            admin.setPassword(passwordEncoder.encode("Admin@123"));
	            admin.setRole(Role.ADMIN);

	            userRepository.save(admin);

	            System.out.println("Admin user created successfully");
	        }
	        else {
	            System.out.println("Admin already exists");
	        }

	    };
	}

}
	