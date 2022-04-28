package com.example.app.domain;

import lombok.Data;

import java.time.LocalDate;

public interface TaskRepartition {
         long getTask_id();
         String getTask_name();
         LocalDate getTask_start_date();
         LocalDate getTask_end_date();
         long getProject_id();
         String getProject_name();
         long getClient_id();
         long getEmployee_id();
         String getUsername();
}
