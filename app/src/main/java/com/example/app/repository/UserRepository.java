package com.example.app.repository;

import com.example.app.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsernameIgnoreCase(String username);

    String findByRolename(String rolename);

    @Query(value="SELECT task_id,task_description as task_description,task_end_date as task_end_date,task_name as task_name,task_start_date as task_start_date,project_id,employee_id,description as project_description, end_date as project_end_date, name as project_name,priority as project_priority, start_date as project_start_date, status as project_status,client_id \n" +
            " FROM\n" +

            "(SELECT id as task_id,description as task_description,end_date as task_end_date,name as task_name,start_date as task_start_date,project_id,employee_id\n" +
            "FROM task\n" +
            "INNER JOIN employee_task as et\n" +
            "ON et.task_id = task.id\n" +
            "\n" +
            "WHERE employee_id = :id \n" +
            "AND end_date >= :date)\n" +
            "as result\n" +
            "INNER JOIN project\n" +
            "ON result.project_id=id ", nativeQuery = true)
    //fetch current tasks and projects related to a specific user (id) starting from a specific (date)
    List<TaskAndProject> getCurrentTasksAndProjects(@Param("id") long id, @Param("date") LocalDate date);

    @Query(value="SELECT\n" +
            "COUNT(CASE WHEN UPPER(Gender)='MALE' THEN 1  END) As Male,\n" +
            "COUNT(CASE WHEN UPPER(Gender)='FEMALE' THEN 1  END) As Female,\n" +
            "COUNT(CASE WHEN UPPER(Rolename) !='ADMIN' THEN 1 END) as Total\n" +
            "FROM employee \n", nativeQuery = true)
    GenderRepartition getGenderRepartition();

    //this query gives repartition per departement
         /*"SELECT department,\n"+
        "COUNT(CASE WHEN UPPER(Gender)='MALE' THEN 1  END) As Male,\n"+
        "COUNT(CASE WHEN UPPER(Gender)='FEMALE' THEN 1  END) As Female,\n"+
        "COUNT(*) as Total \n"+
        "FROM employee GROUP BY department \n"+ "HAVING COUNT(*) = COUNT(department)" */


    @Query(value="SELECT * FROM employee \n" +
            "WHERE\n" +
            "DATE_PART('day', birthdate) = date_part('day', CURRENT_DATE)\n" +
            "AND\n" +
            "DATE_PART('month', birthdate) = date_part('month', CURRENT_DATE)", nativeQuery = true)
    List<AppUser> getUsersBirthdays();

}

