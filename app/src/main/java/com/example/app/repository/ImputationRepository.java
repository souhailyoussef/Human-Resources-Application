package com.example.app.repository;

import com.example.app.domain.Imputation;
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
}
