package com.example.app.service;

import com.example.app.domain.Role;
import com.example.app.domain.AppUser;

import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username,String roleName);
    AppUser getUser(String username);
    //usernames are unique!!
    List<AppUser> getUsers(); //if number of users is too much, return pages instead

}
