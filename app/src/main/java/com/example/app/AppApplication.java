package com.example.app;

import com.example.app.domain.FileDB;
import com.example.app.domain.Node;
import com.example.app.domain.AppUser;
import com.example.app.domain.TreeNode;
import com.example.app.repository.FileDBRepository;
import com.example.app.repository.NodeRepository;
import com.example.app.service.ClientService;
import com.example.app.service.FileDBService;
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
import java.util.ArrayList;
import java.util.HashMap;
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


			/*String currentRelativePath = Paths.get(".").toAbsolutePath().normalize().toString().concat("\\src\\main\\java\\scripts\\script_test.groovy");
			Path fileName = Path.of(currentRelativePath);
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

	@Transactional
	@Bean
	CommandLineRunner run_again(NodeService nodeService) { //this runs after the app is initialized, so no need for
		//manual input
		return args -> {

			Node root = new Node();
			nodeService.saveNode(root);
			//String currentRelativePath = Paths.get(".").toAbsolutePath().normalize().toString().concat("\\src\\main\\resources\\static\\script_somme.groovy");
			//Path path = Path.of(currentRelativePath);
			/*byte[] content = null;
			try {
				content = Files.readAllBytes(path);
				MultipartFile result = new MockMultipartFile("script_somme.groovy",
						"script_somme.groovy","application/octet-stream", content);

				fileDBService.saveFile(result);
			} catch (final IOException e) {
			}
			catch (Exception e)  {System.out.println(e.getMessage());}
	*/
			/*
			try {
				var binding = new Binding();
				binding.setVariable( "fileDBService", fileDBService );
				binding.setVariable( "a", 2 );
				GroovyShell gs = new GroovyShell( binding );
				fileDBService.executeScript(27,binding);
			}
			catch (Exception e ) {
				System.out.println(e.getMessage());
			}
*/

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

				//String currentRelativePath = Paths.get(".").toAbsolutePath().normalize().toString().concat("\\src\\main\\java\\scripts\\script_somme.groovy");
				//Path fileName = Path.of(currentRelativePath);
			//	String content = Files.readString(fileName);
				//var  file = fileDBRepository.findByName("script_somme.groovy").getData();
				//var  file = fileDBService.loadFileByNodeId(2).getData();

				//gs.evaluate(content);
				//this works!







		};
	}




}









