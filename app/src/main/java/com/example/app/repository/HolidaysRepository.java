package com.example.app.repository;

import com.example.app.domain.Holidays;
import com.example.app.domain.Imputation;
import com.example.app.domain.MonthlyHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HolidaysRepository extends JpaRepository<Holidays, Long> {
    Holidays findById(long id);

    @Query(value="SELECT \n" +
            "  CAST(array_agg(dow) as varchar(255)) AS days\n" +
            "FROM (\n" +
            "  SELECT *,  \n" +
            "    trim(\n" +
            "      to_char(\n" +
            "        generate_series(lower(overlap), upper(overlap),'1 day'),\n" +
            "      'Day')) AS dow\n" +
            "  FROM holidays\n" +
            "  CROSS JOIN LATERAL (SELECT tsrange(start_date,end_date,'[]') * \n" +
            "                             tsrange(:start_date, :end_date,'[]')) t (overlap)\n" +
            "  WHERE tsrange(start_date,end_date,'[]') && tsrange(:start_date, :end_date,'[]')) j\n",nativeQuery = true)
            //"GROUP BY id,name,start_date,end_date,number_of_days;",nativeQuery = true)
    String getHolidays(@Param("start_date") LocalDate start_date, @Param("end_date")LocalDate end_date);
    //returns days of the week where there is holidays


    @Query(value="select count(*) as duration, to_char(day,'W') as week from (select generate_series(start_date,end_date, interval '1 day') as day from holidays) as holidays\n" +
            "where day between :start_date and :end_date\n" +
            "group by week",nativeQuery = true)
    List<MonthlyHoliday> getMonthlyHolidays(@Param("start_date") LocalDate start_date, @Param("end_date")LocalDate end_date);
}
