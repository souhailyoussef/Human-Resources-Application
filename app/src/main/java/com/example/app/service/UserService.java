package com.example.app.service;

import com.example.app.domain.AppUser;
import com.example.app.domain.Task;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    void addRoleToUser(String username,String roleName);
    AppUser getUser(String username);
    //usernames are unique!!
    List<AppUser> getUsers(); //if number of users is too much, return pages instead
    AppUser addTaskToUser(long task_id, String username);


    List<AppUser> getUsersBirthdays();
}
