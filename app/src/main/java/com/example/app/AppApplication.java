package com.example.app;

import com.example.app.domain.Role;
import com.example.app.domain.AppUser;
import com.example.app.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.ArrayList;

@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Bean
	CommandLineRunner run(UserService userService) { //this runs after the app is initialized, so no need for
		//manual input
		return args -> {
			userService.saveRole(new Role(null, "COLLABORATOR"));
			userService.saveRole(new Role(null, "ADMIN"));
			userService.saveRole(new Role(null, "PROJECT_MANAGER"));
			userService.saveRole(new Role(null, "MANAGER"));
			userService.saveRole(new Role(null, "ACCOUNTANT"));

			userService.saveUser(new AppUser(null,"John Doe","Tom","1234",null,new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Michael","Michael","xyz",true,new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Emma Stone","Emma","1234",true,new ArrayList<>()));
			userService.saveUser(new AppUser(null,"Jack Smith","Jack","a1b2c3",false,new ArrayList<>()));

			userService.addRoleToUser("Tom","MANAGER");
			userService.addRoleToUser("Jack","COMPTABLE");
			userService.addRoleToUser("Emma","COLLABORATOR");
			userService.addRoleToUser("Michael","MANAGER");



		};
	}




}
