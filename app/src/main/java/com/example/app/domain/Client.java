package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private String mail;
    private String country;
    @OneToMany(mappedBy="client",fetch = FetchType.EAGER)
    private List<Project> projects;

}
