package com.example.app.repository;

import com.example.app.domain.AppUser;
import com.example.app.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRolename(String name);
}
