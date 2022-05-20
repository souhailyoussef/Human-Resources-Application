package com.example.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Imputation {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "task_id")
    private Task task;

    private LocalDate day;
    private Double workload;
    private String status="awaiting confirmation";
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "employee_id")
    @JsonIgnore
    private AppUser employee;

    public Imputation(Task task,LocalDate date, Double workload, String comment , AppUser employee) {
        this.task=task;
        this.day=date;
        this.workload=workload;
        this.comment=comment;
        this.employee=employee;

    }

    public Task getTask() {

        return task;
    }



    /*public String getTask() {
        return task.getName();
    }*/
}

