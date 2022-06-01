package com.example.app.repository;


import com.example.app.domain.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    Leave findById(long id);
    List<Leave> findAll();

    List<Leave> findByEmployeeId(long employee_id);
    void deleteById(Long id);

    @Query(value="SELECT * from leave WHERE approved=true \n " +
            "AND daterange(CAST(start_date AS date), CAST(end_date AS date), '[]') && daterange( :start_date , :end_date, '[]')\n"+
            "AND employee_id= :employee_id",nativeQuery = true)
    List<Leave> findApprovedLeaves(@Param("employee_id") long employee_id,@Param("start_date") LocalDate start_date,@Param("end_date") LocalDate end_date);

    @Query(value="select count(*)>0\n" +
            "from leave\n" +
            "where daterange(CAST(start_date AS date), CAST(end_date as date), '[]') && daterange(:initial_date, :final_date,'[]')\n"+
            "AND employee_id = :id \n"+
            "",nativeQuery = true)
    boolean checkForOverlap(@Param("initial_date") LocalDate initial_date,@Param("final_date") LocalDate final_date,@Param("id") long id);


}
