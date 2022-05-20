package com.example.app.repository;

import com.example.app.domain.Task;
import com.example.app.domain.TaskDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Task findById(long id);

    @Query(value="select id,description,end_date,name,start_date,project_id,status,tag,phase_id,task_id  from task\n" +
            "inner join employee_task\n" +
            "on task_id=id\n" +
            "where employee_id = (select id from employee where lower(username)= :username )",nativeQuery = true)
    List<TaskDetails> getTasksByUsername(@Param("username") String username);


}
