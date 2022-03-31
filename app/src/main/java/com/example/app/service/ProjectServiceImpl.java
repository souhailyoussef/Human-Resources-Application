package com.example.app.service;

import com.example.app.domain.Project;
import com.example.app.repository.ClientRepository;
import com.example.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;


    @Override
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project getProject(long id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }
}
