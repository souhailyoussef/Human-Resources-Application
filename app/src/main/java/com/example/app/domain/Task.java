package com.example.app.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Task {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private String name;
    private String description;

    private LocalDate start_date;
    private LocalDate end_date;
    private String tag;
    private String status;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_task",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<AppUser> employees;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "phase_id")
    private Phase phase;

    @JsonIgnore
    @OneToMany(mappedBy="task")
    private List<Imputation> imputations;



    public Task(long id,String name) {
        this.id=id;
        this.name=name;
    }

    public void setPhase(Phase phase) {
        this.phase=phase;
    }


}
