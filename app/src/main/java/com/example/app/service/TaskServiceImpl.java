package com.example.app.service;

import com.example.app.domain.Project;
import com.example.app.domain.Task;
import com.example.app.repository.ProjectRepository;
import com.example.app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    @Override
    public Task saveTask(Task task) {
        log.info("saving task {}", task.getName());
        return taskRepository.save(task);
    }

    @Override
    public void addTaskToProject(long task_id, long project_id) {
        Task task = taskRepository.findById(task_id);
        projectService.addTask(task,project_id);
        log.info("tasks from task service : {}",projectRepository.findById(project_id).getTasks().size());

        taskRepository.save(task);
        log.info("task successfully added!");
    }

    @Override
    public Task getTask(long id) {
        return taskRepository.findById(id);
    }
}
