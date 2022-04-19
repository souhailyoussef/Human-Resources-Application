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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="client_id",nullable = false)
    private Client client;


    @OneToOne(mappedBy = "project")
    private Contract contract;

    @JsonManagedReference
    @OneToMany(mappedBy="project",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Task> tasks;

    public void addTask(Task task) {

        this.tasks.add(task);
        task.setProject(this);
    }



}
