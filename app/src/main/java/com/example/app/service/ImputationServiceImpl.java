package com.example.app.service;

import com.example.app.domain.*;
import com.example.app.repository.HolidaysRepository;
import com.example.app.repository.ImputationRepository;
import com.example.app.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImputationServiceImpl implements ImputationService{

    private final ImputationRepository imputationRepository;
    private final UserService userService;
    private final LeaveRepository leaveRepository;
    private final HolidaysRepository holidaysRepository;

    @Override
    public Imputation getImputation(long id) {
        log.info("fetching imputation {}", id);
        return imputationRepository.findById(id);
    }

    @Override
    public Imputation saveImputation(Imputation imputation) {
        //,imputation.getTask(imputation.getId())
        log.info("saving imputation for task and user {}", imputation.getEmployee().getUsername());
        return imputationRepository.save(imputation);
    }

    @Override
    public List<Imputation> getImputations(String username, LocalDate start_date, LocalDate end_date) {
        long employee_id = userService.getUser(username).getId();
        return imputationRepository.getImputations(employee_id,start_date,end_date);
    }

    @Override
    public Double getWorkloadPerDay(LocalDate date, long id) {
        Double workload= this.imputationRepository.getWorkloadPerDay(date,id);
        if (workload==null) return Double.valueOf(0);
        else return workload ;
    }

    @Override
    public Imputation updateImputation(long imputation_id, double workload, String comment) {
        Imputation imputation = imputationRepository.findById(imputation_id);
        imputation.setWorkload(workload);
        if (comment!=null) {
            imputation.setComment(comment);
        }
        return imputation;
    }

    @Override
    public Imputation checkExistingImputation(LocalDate date, Long employee_id, Long task_id) {
        return imputationRepository.checkExistingImputation(date,employee_id,task_id);
    }

    @Override
    public List<DailyImputation> getWeeklyImputations(LocalDate start_date, LocalDate end_date, String username) {
        long employee_id = userService.getUser(username).getId();
        return imputationRepository.getWeeklyImputations(start_date,end_date,employee_id);
    }

    @Override
    public List<MonthlyImputation> getMonthlyImputations(LocalDate start_date, LocalDate end_date, Long project_id) {
        return imputationRepository.getMonthlyImputations(start_date,end_date,project_id);
    }

    //LEAVES
    @Override
    public Leave getLeave(long id) {
        log.info("fetching leave with id {}", id);
        return leaveRepository.findById(id);
    }

    @Override
    public Leave saveLeave(Leave leave) {
        var start = leave.getStart_date();
        var end = leave.getEnd_date();
        //boolean check = leaveRepository.checkForOverlap(leave.getStart_date(),leave.getEnd_date(),leave.getEmployee().getId());
        if ( end.compareTo(start)<0) return null;
        log.info("saving new leave");
        //TODO : check logic for duration!!!
        Leave saved_leave =leaveRepository.save(leave);
        log.info("my leave id {}", saved_leave.getId());
        leave.setNumberOfDays(leaveRepository.calculateLeaveDuration(saved_leave.getId()));
        return saved_leave;
    }

    @Override
    public List<Leave> findByEmployeeId(long employee_id) {
        log.info("fetching leave for employee id {}", employee_id);
        return leaveRepository.findByEmployeeId(employee_id);
    }

    @Override
    public List<DailyLeave> findApprovedLeaves(long employee_id, LocalDate start_date, LocalDate end_date) {
        return leaveRepository.getWeeklyLeaves(employee_id,start_date,end_date);
    }

    @Override
    public List<Leave> findAll() {return leaveRepository.findAll();}

    @Override
    public Holidays getHolidays(long id) {
        log.info("fetching specific holiday {}", id);
        return holidaysRepository.findById(id);
    }

    @Override
    public Holidays saveHolidays(Holidays holidays) {
        log.info("saving new holiday into DB");
        return holidaysRepository.save(holidays);
    }

    @Override
    public String findHolidays(LocalDate start_date, LocalDate end_date) {
        log.info("fetching holidays between {} -> {}", start_date,end_date);
        return holidaysRepository.getHolidays(start_date,end_date);
    }

    @Override
    public List<Double> getMonthlyNonBusinessDays(long employee_id, LocalDate start_date, LocalDate end_date) {
        List<MonthlyLeave> leaves = leaveRepository.getMonthlyLeaves(employee_id,start_date,end_date);
        List<MonthlyHoliday> holidays = holidaysRepository.getMonthlyHolidays(start_date,end_date);
        List<Double> nonBusinessDays = Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0);


        for(int i=0;i < leaves.size(); i++) {
            var current_week = Integer.parseInt(leaves.get(i).getWeek().trim());
            var new_value= leaves.get(i).getLeave_duration() + nonBusinessDays.get(current_week);
            nonBusinessDays.set(current_week,new_value);
        }
        for(int i=0;i < holidays.size(); i++) {
            var current_week = Integer.parseInt(holidays.get(i).getWeek().trim());
            var new_value= holidays.get(i).getDuration() + nonBusinessDays.get(current_week);
            nonBusinessDays.set(current_week,new_value);
        }

        Double sum = nonBusinessDays.stream()
                .reduce(0.0, (a, b) -> a + b);

         nonBusinessDays.set(nonBusinessDays.size() - 1,sum);


        return nonBusinessDays;
    }


}
