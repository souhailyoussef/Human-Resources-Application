package com.example.app.service;

import com.example.app.domain.*;
import com.example.app.repository.TaskRepository;
import com.example.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static java.time.LocalTime.now;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TaskRepository taskRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsernameIgnoreCase(username);
        log.error("youre looking for {}", username);
        if (appUser ==null) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        }
        else {
            log.info("User found in databse: {}", username);
        }
       /* Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });*/
        Collection<SimpleGrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(appUser.getRolename()));
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(),appUser.getPassword(),authority);
    }
    @Override
    public AppUser saveUser(AppUser user) {
        log.info("saving new user {} to DB", user.getFirst_name());
        AppUser appUser = userRepository.findByUsernameIgnoreCase(user.getUsername());
        if (appUser != null) {
            log.info("user already exists");
            return null;

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }


    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("adding role {} to user {} in DB",roleName,username);
        AppUser appUser = userRepository.findByUsernameIgnoreCase(username);
        if (appUser.getRolename()==roleName) {
            log.info("Role {} already exists for user {} " , roleName , username);
        }
        else {
            //appUser.getRoles().add(role);
            log.info("trying to add role {} over {}", roleName, appUser.getRolename());
            appUser.setRolename(roleName);
        }

    }

    @Override
    public AppUser getUser(String username) {
        log.info("fetching user {}",username);
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("fetching all users");
        return userRepository.findAll();
    }
    @Override
    public AppUser addTaskToUser(long task_id, String username) {
        Task task = taskRepository.findById(task_id);
        log.info("adding task {} to {}", task.getName(),username);
        AppUser user = userRepository.findByUsernameIgnoreCase(username);
        user.getTasks().add(task);
        return userRepository.save(user);
    }

    @Override
    public List<AppUser> getUsersBirthdays() {
       return  userRepository.getUsersBirthdays();
    }

    @Override
    public List<TaskAndProject> getCurrentTasksAndProjects(long user_id, LocalDate date) {
        return userRepository.getCurrentTasksAndProjects(user_id,date);
    };

    @Override
    public GenderRepartition getGenderRepartition() {
        return userRepository.getGenderRepartition();
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

}
