package com.example.app.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDate;
import java.time.Period;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
public class Holidays {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    long id;

    String name;
    LocalDate start_date;
    LocalDate end_date;
    int number_of_days;


    public Holidays(String name,LocalDate start_date,LocalDate end_date) {
        this.name=name;
        this.start_date=start_date;
        this.end_date=end_date;
        //automatically calculate number_of_days
        Period period = Period.between(start_date, end_date);
        this.number_of_days=period.getDays();
    }

}
