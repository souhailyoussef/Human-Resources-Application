package com.example.app;

import com.example.app.domain.Node;
import com.example.app.domain.AppUser;
import com.example.app.domain.TreeNode;
import com.example.app.repository.NodeRepository;
import com.example.app.service.NodeService;
import com.example.app.service.UserService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovyjarjarantlr4.runtime.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;


@SpringBootApplication
public class AppApplication {

	@Autowired
	private NodeRepository nodeRepository;

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
			//	userService.saveRole("COLLABORATOR");
			//	userService.saveRole("ADMIN");
			//	userService.saveRole("PROJECT_MANAGER");
			//	userService.saveRole("MANAGER");
			//	userService.saveRole("ACCOUNTANT");

			userService.saveUser(new AppUser(null, "John Doe", "John", "1234", null, "COLLABORATORR"));
			userService.saveUser(new AppUser(null, "Michael", "Michael", "xyz", true, "ACCOUNTANT"));
			userService.saveUser(new AppUser(null, "Emma Stone", "Emma", "1234", true, "ADMIN"));
			userService.saveUser(new AppUser(null, "Jack Smith", "Jack", "a1b2c3", false, "MANAGER"));

			userService.addRoleToUser("John", "MANAGER");
			userService.addRoleToUser("Jack", "ACCOUNTANT");
			userService.addRoleToUser("Emma", "COLLABORATOR");
			userService.addRoleToUser("Michael", "MANAGER");


			//String currentRelativePath = Paths.get(".").toAbsolutePath().normalize().toString().concat("\\src\\main\\java\\scripts\\Rubrique_Production.groovy");
			/*Path fileName = Path.of(currentRelativePath);
			String content = Files.readString(fileName);
			GroovyShell shell = new GroovyShell();
			shell.evaluate(content); */


			//Binding binding = new Binding() ;
			//binding.setVariable( "rootNode", rootNode ) ;
			//this creates SRESULT variable in script with value foo
			//System.out.println("binding variable = " + binding.getVariable("rootNode"));;
			//GroovyShell gs = new GroovyShell( binding ) ;
			//gs.evaluate( new File( currentRelativePath ) ) ;  // THIS RUNS THE SCRIPT

		};
	}

	@Bean
	CommandLineRunner run_again(NodeService nodeService) { //this runs after the app is initialized, so no need for
		//manual input
		return args -> {



			Node root = new Node();
			nodeService.saveNode(root);

		};
	}




}









