package com.example.app.repository;

import com.example.app.domain.Holidays;
import com.example.app.domain.Imputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HolidaysRepository extends JpaRepository<Holidays, Long> {
    Holidays findById(long id);

    @Query(value="SELECT * FROM holidays\n" +
            "where daterange(CAST(start_date AS date), CAST(end_date as date), '[]') && daterange(:start_date, :end_date,'[]')\n"
            ,nativeQuery = true)
    List<Holidays> getHolidays(@Param("start_date") LocalDate start_date, @Param("end_date")LocalDate end_date);
}
