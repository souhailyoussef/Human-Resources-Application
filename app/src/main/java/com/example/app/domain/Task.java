package com.example.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;
    private String description;

    private LocalDate start_date;
    private LocalDate end_date;

    @ManyToMany(mappedBy = "tasks")
    private List<AppUser> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "project_id")
    private Project project;


}
