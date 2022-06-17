package com.example.app.repository;


import com.example.app.domain.DailyLeave;
import com.example.app.domain.Leave;
import com.example.app.domain.MonthlyLeave;
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

    @Query(value="select cast(count(*) as decimal)/8 from(select *,extract (dow from h) in (1,2,3,4,5) as business_day\n" +
            "from\n" +
            "(SELECT id,generate_series(start_date \n" +
            ", end_date - interval '1h'\n" +
            ", interval '1h') as h\n" +
            "FROM   leave\n" +
            "group by id) as myleaves\n" +
            "where (cast(h as time) between '08:00' and '11:00'\n" +
            "or cast(h as time) between '14:00' and '17:00')\n" +
            "and checkHoliday(cast(h as date))=false\n" +
            "and id= :leave_id) as leaves_sorted\n" +
            "where business_day",nativeQuery = true)
    Double calculateLeaveDuration(@Param("leave_id") long leave_id);
     /*  this calcuale duration of leave excluding weekends and holidays!!*/


    @Query(value="select count(*) as leave_duration, to_char(h , 'day') as day from(select *,extract (dow from h) in (1,2,3,4,5) as business_day\n" +
            "from\n" +
            "(SELECT employee_id,id,generate_series(start_date \n" +
            ", end_date - interval '1h'\n" +
            ", interval '1h') as h\n" +
            "\n"+
            "FROM leave\n" +
            "where approved=true\n"+
            "group by id) as myleaves\n" +
            "where (cast(h as time) between '08:00' and '11:00'\n" +
            "or cast(h as time) between '14:00' and '17:00')\n" +
            "and checkHoliday(cast(h as date))=false\n" +
            "and h between :start_date and :end_date \n" +
            "and employee_id= :employee_id \n" +
            ") as leaves_sorted\n" +
            "where business_day\n" +
            "group by day",nativeQuery = true)
    List<DailyLeave> getWeeklyLeaves(@Param("employee_id") long employee_id, @Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date);

    @Query(value="select count(*)/8.0 as leave_duration, to_char(h , 'W') as week from(select *,extract (dow from h) in (1,2,3,4,5) as business_day\n" +
            " from\n" +
            "(SELECT employee_id,id,generate_series(start_date\n" +
            ", end_date - interval '1h'\n" +
            ", interval '1h') as h\n" +
            "FROM leave\n" +
            "where approved=true\n" +
            "group by id) as myleaves\n" +
            "where (cast(h as time) between '08:00' and '11:00'\n" +
            "or cast(h as time) between '14:00' and '17:00')\n" +
            "and checkHoliday(cast(h as date))=false\n" +
            "and h between :start_date and :end_date\n" +
            "and employee_id= :employee_id \n" +
            ") as leaves_sorted\n" +
            "where business_day\n" +
            "group by week",nativeQuery = true)
    List<MonthlyLeave> getMonthlyLeaves(@Param("employee_id") long employee_id, @Param("start_date") LocalDate start_date, @Param("end_date") LocalDate end_date);

}
