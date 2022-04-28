package com.example.app.service;

import com.example.app.domain.Project;
import com.example.app.domain.Task;
import com.example.app.repository.ClientRepository;
import com.example.app.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
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

    @Override
    public Project addTask(Task task, long project_id) {
        log.info("adding task to project");
        Project project = projectRepository.findById(project_id);
        log.info("tasks initially : {}",project.getTasks().size());

        project.addTask(task);
        task.setProject(project);
        log.info("tasks after : {}",project.getTasks().size());
         return projectRepository.save(project);
    }

    @Override
    public long countProjects(LocalDate date) {
        return projectRepository.count();
    }

    @Override
    public List<List<Integer>> countCurrentProjectsAndClients() {
        return projectRepository.countCurrentProjectsAndClients();
    }
}
