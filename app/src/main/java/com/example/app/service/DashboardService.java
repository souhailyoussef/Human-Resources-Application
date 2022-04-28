package com.example.app.service;

import com.example.app.domain.Node;
import com.example.app.domain.TaskRepartition;
import com.example.app.domain.WorkdaysRepartition;
import com.example.app.repository.NodeRepository;
import com.example.app.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Service @RequiredArgsConstructor @Transactional @Slf4j @ComponentScan
public class DashboardService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NodeRepository nodeRepository;

    //get list of all employee working on client(client_id) and their tasks
    public List<TaskRepartition> getTaskRepartitionPerClient(long client_id) {
       return userRepository.getTaskRepartitionPerClient(client_id);
    }
    public List<WorkdaysRepartition> getWorkDays(long client_id) {
       var tasks = userRepository.getWorkDaysPerClient(client_id);
       log.debug("{}",tasks);
        return tasks;
    }

    //returns employees and their TJMs per client
    public List<Object> getTJMByClient(long client_id) {
        return userRepository.getTJMByClient(client_id);
    }


    //returns salary of each employee for the whole year (formula = salary brut / number of days worked per month )
    public List<List<Double>> getSalaries() {
        var result =  userRepository.getSalaryHistory();
        if (result.isEmpty()) return new ArrayList<>();
        else return result;
    }

    //returns sum of CDI salaries par month
    public List<Double> getCDISalaries() {
        List<Double> list =  userRepository.getCDISalaries().get(0);
        return list;
    }

    public Node setRubriqueValues(ArrayList<Long> values, long rubrique_id) {
        Node node = nodeRepository.findById(rubrique_id).get();
        node.setValues(values);
        nodeRepository.save(node);
        return node;
    }

}

