package com.example.app.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;


//rename to AppUser after to differenciate from User class in spring security
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="employee")
public class AppUser {
   // public enum Roles {ADMIN,COLLABORATOR,PROJECT_MANAGER,MANAGER,ACCOUNTANT}
    //TODO : add default values for fields that should not be null?
    @Id @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String first_name;
    private String last_name;
    @Column(unique=true)
    private String username;
    private String password;
    private Boolean cnss;
    private String rolename;
    private String gender;
    @Column(columnDefinition = "DATE")
    private LocalDate birthdate;
    private String address;
    private Double salary;
    private String department;



    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_task",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    List<Task> tasks;



}
