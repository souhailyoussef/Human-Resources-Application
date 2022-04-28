package com.example.app.api;

import com.example.app.domain.AppUser;
import com.example.app.domain.Node;
import com.example.app.domain.TaskRepartition;
import com.example.app.domain.WorkdaysRepartition;
import com.example.app.repository.NodeRepository;
import com.example.app.service.DashboardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;
    private final NodeRepository nodeRepository;




    //returns list of working days for employee for a client per month
    @GetMapping(value = "/tasks/{id}", produces = "application/json")
            public ResponseEntity<List<TaskRepartition>> getTaskRepartition(@PathVariable("id") long client_id) {
            var result = dashboardService.getTaskRepartitionPerClient(client_id);
            return ResponseEntity.ok().body(result);
        }
   @GetMapping(value = "/workdays/{client_id}", produces = "application/json")
    public ResponseEntity<List<WorkdaysRepartition>> getWorkDays(@PathVariable("client_id") long client_id) {
        var result = dashboardService.getWorkDays(client_id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="/tjm/{id}", produces = "application/json")
        public ResponseEntity<List<Object>> getTJM(@PathVariable("id") long client_id) {
            var result = dashboardService.getTJMByClient(client_id);
            return ResponseEntity.ok().body(result);
        }


    @GetMapping(value = "/salaries",produces = "application/json")
    public ResponseEntity<List<List<Double>>> getSalaries() {
        var result = dashboardService.getSalaries();
        System.out.println(result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/salaries/cdi",produces = "application/json")
    public ResponseEntity<List<Double>> getCDISalaries() {
        var result = dashboardService.getCDISalaries();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="/node/values/{id}", produces = "application/json")
    public ResponseEntity<List<Long>> getValues(@PathVariable("id") long id) {
        Node node = nodeRepository.findById(id).get();
        return ResponseEntity.ok().body(node.getValues());

    }

    @GetMapping(value="/node/parameters/{id}", produces = "application/json")
    public ResponseEntity<String> getParameters(@PathVariable Long id) {
        Node node = nodeRepository.findById(id).get();
        return ResponseEntity.ok().body(node.getParameters());

    }

    @PostMapping(value="/node/values/{id}", produces = "application/json")
    public ResponseEntity<String> saveValues(@PathVariable Long id, @RequestBody ArrayList<Long> values) {
        log.info("A POST REQUEST HAS BEEN MADE : {}", values.toString());
           try {
            Node node = nodeRepository.findById(id).get();
            node.setValues(values);
            nodeRepository.save(node);
               log.info("VALUES SUCCESSFULLY SAVED INTO RUBRIQUE {}",node.getName());
            return new ResponseEntity<>( "values '"+ values + "' successfully added to rubrique '" + node.getName() + "'",HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping(value="/currency_rate",produces = "application/json")
    public ResponseEntity<List<Double>> getRates() {
        try {
            var rates = nodeRepository.getRates().get(0);
            return ResponseEntity.ok().body(rates);

        }
        catch(Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }


}



