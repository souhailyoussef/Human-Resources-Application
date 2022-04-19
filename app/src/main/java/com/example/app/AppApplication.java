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
			//	userService.saveRole("COLLABORATOR");
			//	userService.saveRole("ADMIN");
			//	userService.saveRole("PROJECT_MANAGER");
			//	userService.saveRole("MANAGER");
			//	userService.saveRole("ACCOUNTANT")
			userService.saveUser(new AppUser(null, "John", "Doe","John", "1234", null, "COLLABORATOR","Male",LocalDate.of(1998,10,22),"sousse",Double.valueOf(1850),"IT",null));
			userService.saveUser(new AppUser(null, "Michael", "Scott", "Michael","xyz", true, "ACCOUNTANT","Male",LocalDate.of(2000,5,10),"sousse",1500.25,"HR",null));
			userService.saveUser(new AppUser(null, "Emma", "Stone","Emma", "1234", true, "ADMIN","Female",LocalDate.of(1997,9,11),"Ben Arous",Double.valueOf(1850),"HR",null));
			userService.saveUser(new AppUser(null, "Jack", "Smith","Jack", "a1b2c3", false, "MANAGER","Male",LocalDate.of(1980,01,31),"Sfax",1200.23,"Business",null));
			userService.saveUser(new AppUser(null,"ADMIN","ADMIN","ADMIN","admin",null,"ADMIN",null,null,null,null,null,null));
			userService.addRoleToUser("John", "MANAGER");
			userService.addRoleToUser("Jack", "ACCOUNTANT");
			userService.addRoleToUser("Emma", "COLLABORATOR");
			userService.addRoleToUser("Michael", "MANAGER");
/*
			taskService.saveTask(new Task(null,"task 1","description for task1",LocalDate.of(2022,04,01),LocalDate.of(2022,04,12),null,null));
			taskService.saveTask(new Task(null,"task 2","description for task2",LocalDate.of(2022,01,01),LocalDate.of(2022,01,28),null,null));
			taskService.saveTask(new Task(null,"task 4","description for task4",LocalDate.of(2022,01,03),LocalDate.of(2022,02,03),null,null));
			taskService.saveTask(new Task(null,"task 5","description for task5",LocalDate.of(2022,02,2),LocalDate.of(2022,02,3),null,null));
			taskService.saveTask(new Task(null,"task 10","description for task10",LocalDate.of(2022,3,15),LocalDate.of(2022,04,27),null,null));
*/
/*
			userService.addTaskToUser(1,"Emma");
			userService.addTaskToUser(2,"Emma");
			userService.addTaskToUser(3,"John");
			userService.addTaskToUser(4,"Michael");
			userService.addTaskToUser(5,"jack");
*/
			/*
			projectService.addTask(taskService.getTask(1),1);
			projectService.addTask(taskService.getTask(2),1);
			projectService.addTask(taskService.getTask(3),2);
			projectService.addTask(taskService.getTask(4),2);
			projectService.addTask(taskService.getTask(5),4);
			projectService.addTask(taskService.getTask(5),3);
			projectService.addTask(taskService.getTask(5),5);
*/

//			var projects = userService.getCurrentTasksAndProjects(3,LocalDate.of(1998,10,22));
			//System.out.println(userService.getUsersBirthdays().get(0).getUsername());

/*
			userService.addTaskToUser(1,"Emma");
			userService.addTaskToUser(2,"Emma");
			userService.addTaskToUser(3,"John");
			userService.addTaskToUser(4,"Michael");
			userService.addTaskToUser(5,"jack");
			System.out.println(userService.getUser("emma").getTasks().size());

*/






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









