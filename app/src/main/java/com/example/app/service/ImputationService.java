package com.example.app.service;

import com.example.app.domain.*;

import java.time.LocalDate;
import java.util.List;


public interface ImputationService {
    Imputation getImputation(long id);
    Imputation saveImputation(Imputation imputation);
    List<Imputation> getImputations(String username, LocalDate start_date,LocalDate end_date);
    Double getWorkloadPerDay(LocalDate date,long id);
    Imputation updateImputation(long imputation_id, double workload, String comment);
    Imputation checkExistingImputation(LocalDate date, Long employee_id, Long task_id);
    List<DailyImputation> getWeeklyImputations(LocalDate start_date,LocalDate end_date, String username);
    List<MonthlyImputation> getMonthlyImputations(LocalDate start_date,LocalDate end_date, Long project_id);

    //leaves
    Leave getLeave(long id);
    Leave saveLeave(Leave leave);
    List<Leave> findByEmployeeId(long employee_id);
    List<DailyLeave> findApprovedLeaves(long employee_id, LocalDate start_date, LocalDate end_date);
    List<Leave> findAll();


    //holidays
    Holidays getHolidays(long id);
    Holidays saveHolidays(Holidays holidays);
    String findHolidays(LocalDate start_date, LocalDate end_date);


    //leaves + holidays
    List<Double> getMonthlyNonBusinessDays(long employee_id, LocalDate start_date, LocalDate end_date);

}
