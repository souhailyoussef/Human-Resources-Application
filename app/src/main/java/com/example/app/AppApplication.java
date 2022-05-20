package com.example.app;

import com.example.app.domain.*;

import com.example.app.service.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.transaction.Transactional;

import java.io.IOException;

import java.time.LocalDate;




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

			userService.saveUser(new AppUser(null, "John", "Doe","John", "1234", null, "COLLABORATOR","Male",LocalDate.of(1998,10,22),"sousse",Double.valueOf(1850),"IT","CDI",true,null,null,null,null));
			userService.saveUser(new AppUser(null, "Michael", "Scott", "Michael","xyz", true, "ACCOUNTANT","Male",LocalDate.of(2000,5,10),"sousse",1500.25,"HR","CDI",true,null,null,null,null));
			userService.saveUser(new AppUser(null, "Emma", "Stone","Emma", "1234", true, "ADMIN","Female",LocalDate.of(1997,9,11),"Ben Arous",Double.valueOf(1850),"HR","CDI",true,null,null,null,null));
			userService.saveUser(new AppUser(null, "Jack", "Smith","Jack", "a1b2c3", false, "MANAGER","Male",LocalDate.of(1980,1,31),"Sfax",1200.23,"Business","CDI",true,null,null,null,null));
			userService.saveUser(new AppUser(null,"ADMIN","ADMIN","ADMIN","admin",null,"ADMIN",null,null,null,null,null,null,true,null,null,null,null));
			userService.addRoleToUser("John", "MANAGER");
			userService.addRoleToUser("Jack", "ACCOUNTANT");
			userService.addRoleToUser("Emma", "COLLABORATOR");
			userService.addRoleToUser("Michael", "MANAGER");

		};
	}


	@Transactional
	@Bean
	CommandLineRunner run_again(NodeService nodeService) {
		//manual input
		return args -> {

			Node root = new Node();
			nodeService.saveNode(root);
			//runScripts();


		};
	}

	public void runScripts() throws IOException {
/*
		GroovyShell groovyShell = new GroovyShell();
		List<FileDB> scripts = fileDBService.getScripts();
		for (FileDB script : scripts) {
			System.out.println("running script : " + script.getName());
			groovyShell.evaluate(new String(script.getData(), StandardCharsets.UTF_8));

		}
*/

	}





}









