package com.example.app.domain;

import lombok.Data;

import java.time.LocalDate;

public interface TaskAndProject {
     long getTask_id();
     String getTask_description();
     LocalDate getTask_end_date();
     String getTask_name();
     LocalDate getTask_start_date();
     long getProject_id();
     long getEmployee_id();
     String getProject_description();
     String getProject_name();
     LocalDate getProject_end_date();
     int getProject_priority();
     LocalDate getProject_start_date();
     String getProject_status();
     long getClient_id();


}