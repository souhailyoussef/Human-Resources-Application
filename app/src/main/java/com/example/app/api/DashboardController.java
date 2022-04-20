package com.example.app.api;

import com.example.app.domain.AppUser;
import com.example.app.domain.Node;
import com.example.app.repository.NodeRepository;
import com.example.app.service.DashboardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/hi")
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok().body("hi");
    }


    //returns list of working days for employee for a client per month
    @GetMapping(value = "/workdays", produces = "application/json")
        public ResponseEntity<List<Integer>> getWorkDays(@RequestParam long id, @RequestParam long client_id) {
            var result = dashboardService.getWorkDaysPerMonth(id,client_id);
            return ResponseEntity.ok().body(result);
        }
    @GetMapping(value="/TJM", produces = "application/json")
        public ResponseEntity<List<Integer>> getTJM(@RequestParam long id, @RequestParam long client_id) {
            var result = dashboardService.getTJM(id,client_id);
            return ResponseEntity.ok().body(result);
        }
    @GetMapping(value = "/salary",produces = "application/json")
    public ResponseEntity<List<Integer>> getSalary(@RequestParam long id) {
        var result = dashboardService.getSalary(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value="/node/values/{id}", produces = "application/json")
    public ResponseEntity<List<Long>> getValues(@PathVariable Long id) {
        Node node = nodeRepository.findById(id).get();
        return ResponseEntity.ok().body(node.getValues());

    }

    @GetMapping(value="/node/parameters/{id}", produces = "application/json")
    public ResponseEntity<String> getParameters(@PathVariable Long id) {
        Node node = nodeRepository.findById(id).get();
        return ResponseEntity.ok().body(node.getParameters());

    }

    @PostMapping(value="/node/values/{id}", produces = "application/json")
    public ResponseEntity<String> saveValues(@PathVariable Long id, @RequestBody Values values) {
        if (values.getValues().equals("hello")) {
            return ResponseEntity.ok().body("hello yourself");

        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
     /*   try {
            Node node = nodeRepository.findById(id).get();
            node.setValues(values);
            nodeRepository.save(node);
            return new ResponseEntity<>(values.toString(),HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }*/


    }


}

@Data
class Values {
    private String values;

    public String getValues() {return values;}
}
