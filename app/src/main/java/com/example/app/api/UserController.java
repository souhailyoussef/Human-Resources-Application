package com.example.app.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.app.domain.AppUser;
import com.example.app.domain.GenderRepartition;
import com.example.app.domain.TaskAndProject;
import com.example.app.service.ProjectService;
import com.example.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequiredArgsConstructor @RequestMapping(value = "/api" )

@CrossOrigin(origins = "http://localhost:4200")
public class UserController {


    private final UserService userService;
    private final ProjectService projectService;

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
    public ResponseEntity<List<TaskAndProject>> getCurrentTasksAndProjects(@RequestParam long id) {
        LocalDate date = LocalDate.now();
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


