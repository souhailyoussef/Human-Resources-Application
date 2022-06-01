package com.example.app.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.app.domain.*;
import com.example.app.service.ImputationService;
import com.example.app.service.ProjectService;
import com.example.app.service.TaskService;
import com.example.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.readOnlyHttpHeaders;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequiredArgsConstructor @RequestMapping(value = "/api" )

@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final ImputationService imputationService;

    @GetMapping("/projects")
    public ResponseEntity<?> getProjects() {
        return ResponseEntity.ok().body(projectService.getProjects());
    }
    //TODO : move this to another controller


    @GetMapping("/imputations")
    public ResponseEntity<?> getImputations(@RequestParam String username,@RequestParam String start_date,@RequestParam String end_date) {
        LocalDate start = LocalDate.parse(start_date);
        LocalDate end = LocalDate.parse(end_date);
        return ResponseEntity.ok().body(imputationService.getImputations(username, start, end));
    }
    @GetMapping("/check")
    public ResponseEntity<?> getExisting(@RequestParam String date,@RequestParam long employee,@RequestParam long task) {
        LocalDate myDate = LocalDate.parse(date);
        return ResponseEntity.ok().body(imputationService.checkExistingImputation(myDate, employee, task));
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/imputation")
    public ResponseEntity<?> confirmImputation(@RequestBody ConfirmImputationForm form) {
        Imputation imputation = imputationService.getImputation(form.getId());
        if (form.isConfirm()) {
            imputation.setStatus("confirmed");
            imputationService.saveImputation(imputation);
            return ResponseEntity.ok().body("confirmed");
        }
        imputation.setStatus("refused");
        imputationService.saveImputation(imputation);
        return ResponseEntity.ok().body("refused");
    }


    @PostMapping("/imputation/save")
    //TODO  : fix random errors!
    public ResponseEntity<?> saveImputation(@RequestBody ImputationForm imputationForm) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/imputation/save").toUriString());
        Task task = taskService.getTask(imputationForm.getTask_id());
        AppUser appuser = userService.getUser(imputationForm.getUsername());
        double total_workload = imputationService.getWorkloadPerDay(LocalDate.parse(imputationForm.getDay()), appuser.getId());
        Imputation existing_imputation = imputationService.checkExistingImputation(LocalDate.parse(imputationForm.getDay()),Long.valueOf(appuser.getId()),imputationForm.getTask_id());

        System.out.println("total workload : "+total_workload);
        //CACULATE THE WORKLOAD FOR THAT DAY before updating + reset selected cells on click + only unique cells
        if (existing_imputation!=null)  {
            //we update it
            double oldWorkload=existing_imputation.getWorkload();
            if (total_workload +imputationForm.getWorkload() - oldWorkload <= 8 && imputationForm.getWorkload() >= 0) {
                System.out.println("updating the value ");
                return ResponseEntity.ok().body(imputationService.updateImputation(existing_imputation.getId(),Double.valueOf(imputationForm.getWorkload()),imputationForm.getComment()));

            }
            return ResponseEntity.status(FORBIDDEN).body(new ServerResponse("maximum work hours per day surpassed"));
        }
        else if (existing_imputation==null && (total_workload + imputationForm.getWorkload() <= 8) &&  imputationForm.getWorkload() >= 0 ) {
            Imputation imputation = new Imputation(task, LocalDate.parse(imputationForm.getDay()), imputationForm.getWorkload(), imputationForm.getComment(), appuser);
            return ResponseEntity.created(uri).body(imputationService.saveImputation(imputation));

        }
        else {
            return ResponseEntity.status(FORBIDDEN).body(new ServerResponse("something went wrong"));
        }


    }

    @GetMapping("/leaves")
    public ResponseEntity<List<Leave>>getLeaves() {
        return ResponseEntity.ok().body(imputationService.findAll());
    }

    @GetMapping("/leaves/{username}")
    public ResponseEntity<List<Leave>>getLeavesByEmployee(@PathVariable String username) {
        AppUser employee = userService.getUser(username);
        return ResponseEntity.ok().body(imputationService.findByEmployeeId(employee.getId()));
    }
    @GetMapping("/leaves/approved/{username}")
    public ResponseEntity<List<Leave>>getApprovedLeavesByEmployee(@PathVariable String username,@RequestParam String start_date,
                                                                  @RequestParam String end_date) {
        LocalDate from = LocalDate.parse(start_date);
        LocalDate to = LocalDate.parse(end_date);
        AppUser employee = userService.getUser(username);
        return ResponseEntity.ok().body(imputationService.findApprovedLeaves(employee.getId(),from,to));
    }

    @PostMapping("/leaves/save")
    public ResponseEntity<?> postLeave(@RequestBody LeaveForm leaveForm) {
        Timestamp start_date = Timestamp.valueOf(leaveForm.getStart_date());
        Timestamp end_date = Timestamp.valueOf(leaveForm.getEnd_date());

        Date start = new Date(start_date.getTime());
        Date end = new Date(end_date.getTime());
        System.out.println("dates");
        System.out.println(start);
        System.out.println(end);
        AppUser employee = userService.getUser(leaveForm.getUsername());
        Leave leave = new Leave(start,end,leaveForm.getReason(),employee);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/leaves/save").toUriString());
        var result = imputationService.saveLeave(leave);
        if (result==null) return ResponseEntity.badRequest().body(new ServerResponse("dates are overlapping with an existing leave"));
        return ResponseEntity.created(uri).body(imputationService.saveLeave(leave));
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<Holidays>>getHolidays(@RequestParam String start_date,@RequestParam String end_date) {
        LocalDate from = LocalDate.parse(start_date);
        LocalDate to = LocalDate.parse(end_date);
        return ResponseEntity.ok().body(imputationService.findHolidays(from,to));
    }


    @GetMapping("/users")
    public ResponseEntity<List<AppUser>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @GetMapping("/users/birthdays")
    public ResponseEntity<List<AppUser>>getUsersBirthdays() {
        return ResponseEntity.ok().body(userService.getUsersBirthdays());
    }

    @GetMapping("/users/gender")
    public ResponseEntity<GenderRepartition>getGenderRepartition() {
        return ResponseEntity.ok().body(userService.getGenderRepartition());
    }
    @GetMapping("/user/projects")
    public ResponseEntity<List<TaskAndProject>> getCurrentTasksAndProjects(@RequestParam long id,@RequestParam String deadline) {
        LocalDate date = LocalDate.parse(deadline);
        return ResponseEntity.ok().body( userService.getCurrentTasksAndProjects(id,date));
    }





    @GetMapping(value = "/users/user/{username}", produces = "application/json")
    public ResponseEntity<AppUser>getUser(@PathVariable String username, HttpServletResponse response) {
        AppUser user = userService.getUser(username);
        if (user == null) {
           // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("username : {} does not exist!" + username);
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(userService.getUser(username));
    }
    @GetMapping(value="/user/{username}/tasks", produces = "application/json")
    public ResponseEntity<?> getTasks(@PathVariable String username) {
        var tasks = taskService.getTasksByUsername(username);
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser>saveUser(@RequestBody AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(),form.getRoleName());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader!= null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm= Algorithm.HMAC256("secret".getBytes()); //TODO : save algorithm in a utility class
                JWTVerifier jwtVerifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);
                String access_token= JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 60*60*1000)) //token expires after 10 mns
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRolename())
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);

            }
            catch (Exception exception) {
                response.setHeader("error",exception.getMessage());
                //response.sendError(FORBIDDEN.value());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping(value = "/dashboard/info", produces = "application/json")
    public ResponseEntity<GeneralInfo> getDashboardInfo() {
        List<Integer> list = projectService.countCurrentProjectsAndClients().get(0);
        list.add((int)userService.countUsers());
        GeneralInfo result = new GeneralInfo(list.get(0),list.get(1),list.get(2));

        return ResponseEntity.ok().body(result);
    }


}
@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}
@Data
class ConfirmImputationForm {
    private long id;
    private boolean confirm;
}
@Data
class ImputationForm {
    private long task_id;
    private String day;
    private double workload;
    private String comment;
    private String username;
}
//TODO : make controller accept json format and remove this class
@Data
class Username {
    private String username;
}

@Data
class GeneralInfo{
    private Integer clients;
    private Integer projects;
    private Integer employees;
    public GeneralInfo(Integer clients, Integer projects, Integer employees) {
        this.clients=clients;
        this.projects=projects;
        this.employees=employees;
    }
}
@Data
class LeaveForm {
    private String start_date;
    private String end_date;
    private String reason;
    private boolean approved;
    private String username;

}
@Data
class ServerResponse {

    private String response;

    public ServerResponse(String s) {
        this.response = s;
    }

    // get/set omitted...
}


