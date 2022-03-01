package com.example.app.repository;

import com.example.app.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsernameIgnoreCase(String username);
}
