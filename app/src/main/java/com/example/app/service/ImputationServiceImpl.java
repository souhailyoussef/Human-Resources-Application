package com.example.app.service;

import com.example.app.domain.Holidays;
import com.example.app.domain.Imputation;
import com.example.app.domain.Leave;
import com.example.app.repository.HolidaysRepository;
import com.example.app.repository.ImputationRepository;
import com.example.app.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

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
        return leaveRepository.save(leave);
    }

    @Override
    public List<Leave> findByEmployeeId(long employee_id) {
        log.info("fetching leave for employee id {}", employee_id);
        return leaveRepository.findByEmployeeId(employee_id);
    }

    @Override
    public List<Leave> findApprovedLeaves(long employee_id, LocalDate start_date, LocalDate end_date) {
        return leaveRepository.findApprovedLeaves(employee_id,start_date,end_date);
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
    public List<Holidays> findHolidays(LocalDate start_date, LocalDate end_date) {
        log.info("fetching holidays between {} -> {}", start_date,end_date);
        return holidaysRepository.getHolidays(start_date,end_date);
    }


}
