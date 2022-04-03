package com.example.app.service;

import com.example.app.domain.Task;
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

    @Override
    public Task saveTask(Task task) {
        log.info("saving task {}", task.getName());
        return taskRepository.save(task);
    }
}
