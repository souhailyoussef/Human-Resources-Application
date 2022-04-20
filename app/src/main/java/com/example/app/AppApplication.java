package com.example.app;

import com.example.app.domain.*;
import com.example.app.repository.FileDBRepository;
import com.example.app.repository.NodeRepository;
import com.example.app.service.*;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;


@SpringBootApplication
public class AppApplication {

	@Autowired
	private NodeRepository nodeRepository;

	@Autowired
	private FileDBRepository fileDBRepository;

	@Autowired
	private FileDBService fileDBService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ProjectService projectService;

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

			userService.saveUser(new AppUser(null, "John", "Doe","John", "1234", null, "COLLABORATOR","Male",LocalDate.of(1998,10,22),"sousse",Double.valueOf(1850),"IT",null));
			userService.saveUser(new AppUser(null, "Michael", "Scott", "Michael","xyz", true, "ACCOUNTANT","Male",LocalDate.of(2000,5,10),"sousse",1500.25,"HR",null));
			userService.saveUser(new AppUser(null, "Emma", "Stone","Emma", "1234", true, "ADMIN","Female",LocalDate.of(1997,9,11),"Ben Arous",Double.valueOf(1850),"HR",null));
			userService.saveUser(new AppUser(null, "Jack", "Smith","Jack", "a1b2c3", false, "MANAGER","Male",LocalDate.of(1980,01,31),"Sfax",1200.23,"Business",null));
			userService.saveUser(new AppUser(null,"ADMIN","ADMIN","ADMIN","admin",null,"ADMIN",null,null,null,null,null,null));
			userService.addRoleToUser("John", "MANAGER");
			userService.addRoleToUser("Jack", "ACCOUNTANT");
			userService.addRoleToUser("Emma", "COLLABORATOR");
			userService.addRoleToUser("Michael", "MANAGER");

		};
	}


	@Transactional
	@Bean
	CommandLineRunner run_again(NodeService nodeService) { //this runs after the app is initialized, so no need for
		//manual input
		return args -> {

			Node root = new Node();
			nodeService.saveNode(root);


			/*

			try {
				GroovyShell shell = new GroovyShell();
				var binding = new Binding();
				binding.setVariable( "clientService", clientService );
				binding.setVariable( "a", 2 );
				GroovyShell gs = new GroovyShell( binding );
				var file = fileDBRepository.findById(32).getData();
				gs.evaluate(new String(file, StandardCharsets.UTF_8));
			}
			catch (Exception e) {
				System.out.println(e.toString());
			}
*/


		};
	}




}









