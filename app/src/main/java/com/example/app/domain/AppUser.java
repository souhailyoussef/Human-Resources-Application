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
    private Long id;
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
    private String contract;
    private boolean onSite;
    private LocalDate hireDate;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
         name = "team_members",
         joinColumns = @JoinColumn(name = "member_id"),
         inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<Team> teams;



    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_task",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    List<Task> tasks;

    @OneToMany(mappedBy="employee")
    private List<Imputation> imputations;

 @JsonManagedReference
 @OneToMany(mappedBy="employee")
    private List<Leave> leaves;


    public boolean addTask(Task task) {
     if (!tasks.contains(task)) {
      tasks.add(task);
     }
      return false;
    }



    public boolean removeTask(Task task) {
     return tasks.remove(task);
    }
  
}
