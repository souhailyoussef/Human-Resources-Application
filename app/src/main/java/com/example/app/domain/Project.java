package com.example.app.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Date start_date;
    private Date end_date;
    private String status;
    private Integer priority;
    @ManyToOne
    @JoinColumn(name = "project_owner")
    private AppUser projectOwner;
    private String projectType;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="team_id")
    private Team team;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="client_id",nullable = false)
    private Client client;


    @OneToOne(mappedBy = "project")
    private Contract contract;

    @JsonManagedReference
    @OneToMany(mappedBy="project",fetch = EAGER)
    private List<Phase> phases;

    public Project(String projectName, Team team) {
        this.name=projectName;
        this.team=team;
    }

    public void addPhase(Phase phase) {

        this.phases.add(phase);
        phase.setProject(this);
    }








}
