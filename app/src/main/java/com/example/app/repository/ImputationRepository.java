package com.example.app.repository;

import com.example.app.domain.DailyImputation;
import com.example.app.domain.Imputation;
import com.example.app.domain.MonthlyImputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ImputationRepository extends JpaRepository<Imputation, Long> {
    Imputation findById(long id);

    @Query(value="SELECT * FROM imputation\n" +
            "WHERE employee_id= :id \n" +
            "AND day BETWEEN :start_date AND :end_date ",nativeQuery = true)
    List<Imputation> getImputations(@Param("id") long employee_id, @Param("start_date") LocalDate start_date, @Param("end_date")LocalDate end_date);

    @Query(value="select sum(workload) from imputation\n" +
            "where day= :date\n" +
            "group by employee_id\n" +
            "having employee_id= :id",nativeQuery = true)
    Double getWorkloadPerDay(@Param("date") LocalDate date, @Param("id") long id);

    @Query(value="select * from imputation\n" +
            "where day = :date \n" +
            "and employee_id = :employee_id\n" +
            "and task_id = :task_id\n " +
            "LIMIT 1",nativeQuery = true)
    Imputation checkExistingImputation(@Param("date") LocalDate date, @Param("employee_id") long employee_id, @Param("task_id") long task_id);

    @Query(value="select task_id,to_char(day,'day') as dow, workload,name,project_id  from \n" +
            "(select * from imputation\n" +
            "where day between :start_date and :end_date\n" +
            "and employee_id= :employee_id)as ei\n" +
            "inner join task\n" +
            "on task.id=ei.task_id", nativeQuery = true)
    List<DailyImputation> getWeeklyImputations(@Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date, @Param("employee_id") long employee_id);

    @Query(value="select sum(workload) as total, week, employee_id,task_id,task_name,project_id,project_name,client_id,client_name,first_name,last_name,department from\n" +
            "(select to_char(day, 'W') as week,task_id,workload,employee_id,task.name as task_name,task.project_id,project.name as project_name,project.client_id as client_id, client.name as client_name, employee.first_name, employee.last_name, employee.department from imputation\n" +
            "inner join task\n" +
            "on task.id= imputation.task_id\n" +
            "inner join project\n" +
            "on project.id= task.project_id\n" +
            "inner join client\n" +
            "on project.client_id=client.id\n" +
            "inner join employee\n" +
            "on imputation.employee_id=employee.id\n" +
            "where day between :start_date and :end_date\n" +
            "and imputation.status= 'approved'\n" +
            "and project_id= :project_id) as intermediate_table\n" +
            "group by week,employee_id,task_id,task_name,project_id,project_name,client_id,client_name,first_name,last_name,department\n"
            ,nativeQuery = true)
    List<MonthlyImputation> getMonthlyImputations(@Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date, @Param("project_id")long project_id);

}
