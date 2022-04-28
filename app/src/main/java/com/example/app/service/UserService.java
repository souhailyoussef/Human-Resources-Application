package com.example.app.service;

import com.example.app.domain.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    void addRoleToUser(String username,String roleName);
    AppUser getUser(String username);
    //usernames are unique!!
    List<AppUser> getUsers(); //if number of users is too much, return pages instead
    AppUser addTaskToUser(long task_id, String username);


    List<AppUser> getUsersBirthdays();
    List<TaskAndProject> getCurrentTasksAndProjects(long user_id, LocalDate date);

    GenderRepartition getGenderRepartition();
    long countUsers();


}
