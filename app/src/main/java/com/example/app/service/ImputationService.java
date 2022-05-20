package com.example.app.service;

import com.example.app.domain.Imputation;

import java.time.LocalDate;
import java.util.List;


public interface ImputationService {
    Imputation getImputation(long id);
    Imputation saveImputation(Imputation imputation);
    List<Imputation> getImputations(String username, LocalDate start_date,LocalDate end_date);
    Double getWorkloadPerDay(LocalDate date,long id);
    Imputation updateImputation(long imputation_id, double workload, String comment);
    Long checkExistingImputation(LocalDate date, Long employee_id, Long task_id);
}
