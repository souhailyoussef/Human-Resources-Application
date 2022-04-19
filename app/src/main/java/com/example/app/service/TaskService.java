package com.example.app.service;

import com.example.app.domain.Task;

public interface TaskService {
    Task saveTask(Task task);
    void addTaskToProject(long task_id,long project_id);
    Task getTask(long id);
}
