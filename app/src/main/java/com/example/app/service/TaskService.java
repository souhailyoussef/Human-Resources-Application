package com.example.app.service;

import com.example.app.domain.Task;
import com.example.app.domain.TaskDetails;

import java.util.List;

public interface TaskService {

    Task saveTask(Task task);
    void addTaskToPhase(long task_id,long phase_id);
    Task getTask(long id);
    List<TaskDetails> getTasksByUsername(String username);
}
