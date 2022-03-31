package com.example.app.service;

import com.example.app.domain.Project;

import java.util.List;

public interface ProjectService {
    Project saveProject(Project project);
    Project getProject(long id);
    List<Project> getProjects();
}
