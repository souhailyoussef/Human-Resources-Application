package scripts

import org.hibernate.cfg.Environment

import java.time.LocalDate

LocalDate now = LocalDate.now();

println("First day of the month: " + getFirstDateOfMonth(new Date()));


 Date getFirstDateOfMonth(Date date){
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    return cal.getTime();
}