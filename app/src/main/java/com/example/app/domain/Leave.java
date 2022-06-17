package com.example.app.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leave {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="start_date",columnDefinition="TIMESTAMP WITHOUT TIME ZONE")
    private Date start_date;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="end_date",columnDefinition="TIMESTAMP WITHOUT TIME ZONE")
    private Date end_date;
    private String reason;
    private boolean approved;
    @Temporal(value=TemporalType.TIMESTAMP)
    @Column(name="submittedAt",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date submittedAt;

    private String type;

    @Column(name="No_of_days")
    private Double NumberOfDays; // day = 8 hours; 1 hour  = 0.125 day

    //private AppUser approvedBy;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private AppUser employee;

    public Leave(Date start_date,Date end_date, String reason, AppUser employee) {
        this.start_date=start_date;
        this.end_date=end_date;
        this.reason=reason;
        this.approved=true;
        this.employee=employee;
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        this.submittedAt=date;
    }
}
