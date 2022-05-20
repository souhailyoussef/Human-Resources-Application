package com.example.app.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phase {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate start_date;
    private LocalDate end_date;
    private String status;
    private String tag;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER) //add column definitions as needed
    @JoinColumn(name="parent_id")
    private Phase parent;      //each Domain with parent==null is a root domain, all others are subdomains

    @OneToMany(mappedBy = "parent", orphanRemoval = true,fetch=FetchType.EAGER,cascade = CascadeType.REMOVE) //add column definitions as needed
    private List<Phase> subPhases;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "project_id")
    private Project project;

    @JsonManagedReference
    @OneToMany(mappedBy="phase")
    private List<Task> tasks;


    public void setProject(Project project) {

        this.project=project;
    }

    public Phase(final Phase parent, String name) {
        if(parent==null) throw new IllegalArgumentException("parent required");

        this.parent = parent;
        this.name=name;
        this.subPhases = new ArrayList<>();
    }

    public Phase(String name) {
        this.parent=null;
        this.name=name;
        this.subPhases = new ArrayList<>();

    }

    public void addPhase(Phase phase) {

        this.subPhases.add(phase);
    }

    public void setParent(Phase parent) {
        parent.addPhase(this);
        this.parent = parent;
        this.project=parent.project;
    }

    public boolean isRootPhase() {
        return (this.parent == null);
    }

    public boolean isLeafPhase() {
        return (this.subPhases.size() == 0);
    }

    public boolean addTask(Task task) {
        if (!tasks.contains(task)) {
            task.setPhase(this);
            return this.tasks.add(task);
        }
        return false;
    }
    @Override
    public String toString() {
        return "phase " + this.getName() + " | project :  " + (this.project == null ? "None" : this.project.getName())+ " | subPhases : " + this.subPhases + " | tasks : " + this.tasks +  "\n";
    }



}
