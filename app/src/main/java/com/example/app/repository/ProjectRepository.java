package com.example.app.repository;

import com.example.app.domain.Client;
import com.example.app.domain.FileDB;
import com.example.app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findById(long id);
}
