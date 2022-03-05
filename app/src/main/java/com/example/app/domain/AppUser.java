package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.GenerationType.IDENTITY;


//rename to AppUser after to differenciate from User class in spring security
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
   // public enum Roles {ADMIN,COLLABORATOR,PROJECT_MANAGER,MANAGER,ACCOUNTANT}
    //TODO : add default values for fields that should not be null?
    @Id @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String first_name;
    private String username;
    private String password;
    @Column(name="cnss")
    private Boolean cnss;
    private String rolename;


}
