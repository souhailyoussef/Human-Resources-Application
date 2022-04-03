package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    private String name;
    private String description;
    private Date start_date;
    private Date end_date;
    private String status;
    private int priority;

    @ManyToOne
    @JoinColumn(name="client_id",nullable = false)
    private Client client;

    @OneToOne(mappedBy = "project")
    private Contract contract;

    @OneToMany(mappedBy="project",fetch = FetchType.EAGER)
    private List<Task> tasks;




}
