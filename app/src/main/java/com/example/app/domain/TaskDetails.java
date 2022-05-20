package com.example.app.domain;

import java.time.LocalDate;

public interface TaskDetails {
    long getId();
    String getDescription();
    LocalDate getEnd_date();
    String getName();
    LocalDate getStart_date();
    long getProject_id();
    String getStatus();
    String getTag();
    String getPhaseId();
}
