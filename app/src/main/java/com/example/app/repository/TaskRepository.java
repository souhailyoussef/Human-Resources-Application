package com.example.app.repository;

import com.example.app.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Task findById(long id);

}
