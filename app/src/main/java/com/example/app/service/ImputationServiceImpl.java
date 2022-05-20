package com.example.app.service;

import com.example.app.domain.Imputation;
import com.example.app.repository.ImputationRepository;
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
        if (!comment.isBlank()) {
            imputation.setComment(comment);
        }
        return imputation;
    }

    @Override
    public Long checkExistingImputation(LocalDate date, Long employee_id, Long task_id) {
        return imputationRepository.checkExistingImputation(date,employee_id,task_id);
    }
}
