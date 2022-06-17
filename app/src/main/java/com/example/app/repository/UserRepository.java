package com.example.app.repository;

import com.example.app.domain.*;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsernameIgnoreCase(String username);

    String findByRolename(String rolename);

    @Query(value="SELECT task_id,task_description as task_description,task_tag,task_end_date as task_end_date,task_name as task_name,task_start_date as task_start_date,project_id,employee_id,description as project_description, end_date as project_end_date, name as project_name,priority as project_priority, start_date as project_start_date, status as project_status,client_id \n" +
            " FROM\n" +

            "(SELECT id as task_id,tag as task_tag, description as task_description,end_date as task_end_date,name as task_name,start_date as task_start_date,project_id,employee_id\n" +
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

    @Query(value="SELECT * FROM hist_salary", nativeQuery = true)
    List<List<Double>> getSalaryHistory();

    @Query(value="SELECT employee_id, employee_name, january, february, march, april, june, july, august, september, october, november, december\n" +
            "\tFROM tjm WHERE client_id = :client_id",nativeQuery = true)
    List<Object> getTJMByClient(@Param("client_id") long client_id);



    @Query(value="\n" +
            "SELECT SUM(january) as january,SUM(february) as february,SUM(march) as march,SUM(april) as april,SUM(may) as may,SUM(june) as june,SUM(july) as july,SUM(august) as august,SUM(september) as september,SUM(october) as october,SUM(november) as november,SUM(december) as december FROM\n" +
            "(SELECT employee_id,january,february,march,april,may,june,july,august,september,october,november,december,contract FROM hist_salary\n" +
            "inner join employee\n" +
            "on employee.id=hist_salary.employee_id\n" +
            "WHERE UPPER(contract)='CDI')\n" +
            "as result\n", nativeQuery = true)
    List<List<Double>> getCDISalaries();

    @Query(value="SELECT result2.task_id, task_name, task_start_date, task_end_date,project_id,project_name,client_id, employee_id, employee.username from\n"+
   "(SELECT result.task_id, task_name, task_start_date, result.task_end_date,project_id,project_name,client_id, employee_id FROM\n"+
   "(SELECT task.id as task_id, task.name as task_name, task.start_date as task_start_date, task.end_date as task_end_date, project.id as project_id,\n"+
   "project.name as project_name , project.client_id FROM project,task\n"+
   "WHERE project.id=task.project_id)\n"+
    "as result, employee_task WHERE employee_task.task_id=result.task_id AND client_id= :client_id) as result2, employee\n"+
   "WHERE employee.id=result2.employee_id",nativeQuery = true)
    List<TaskRepartition> getTaskRepartitionPerClient(@Param("client_id") long client_id);

    @Query(value="SELECT result2.task_id, task_name, task_start_date, task_end_date,project_id,project_name,client_id, employee_id, employee.username from\n" +
            "(SELECT result.task_id, task_name, task_start_date, task_end_date,project_id,project_name,client_id, employee_id FROM\n" +
            "(SELECT task.id as task_id, task.name as task_name, task.start_date as task_start_date, task.end_date as task_end_date, project.id as project_id,  \n" +
            "project.name as project_name , project.client_id\n" +
            "FROM project,task\n" +
            "WHERE project.id=task.project_id)\n" +
            "as result, employee_task\n" +
            "WHERE employee_task.task_id=result.task_id\n" +
            "AND client_id= :client_id)\n" +
            "as result2, employee\n" +
            "WHERE employee.id=result2.employee_id\n" +
            "AND employee.id= :employee_id",nativeQuery = true)
    List<TaskRepartition> getTaskRepartitionPerClientAndEmployee(@Param("employee_id")long employee_id,@Param("client_id")long client_id);

    @Query(value="SELECT employee_id,\n" +
            "SUM(january) as january,SUM(february) as february, SUM(march) as march, SUM(april) as april, SUM(may) as may, SUM(june) as june, SUM(july) as july, SUM(august) as august, sum(september) as september, sum(october) as october, sum(november)as november, sum(december) as december\n" +
            "from\n" +
            "(\n" +
            "SELECT * FROM\n" +
            "(SELECT t1.id as task_id,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 1 THEN 1 END) as january,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 2 THEN 1 END) as february,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 3 THEN 1 END) as march,\n" +
            "\t   count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 4 THEN 1 END) as april,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 5 THEN 1 END) as may,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 6 THEN 1 END) as june,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 7 THEN 1 END) as july,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 8 THEN 1 END) as august,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 9 THEN 1 END) as september,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 10 THEN 1 END) as october,\n" +
            "\t   count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 11 THEN 1 END) as november,\n" +
            "       count(CASE WHEN extract(isodow from dt) BETWEEN 1 AND 5 AND EXTRACT(MONTH from dt) = 12 THEN 1 END) as december\n" +
            "FROM task as t1\n" +
            "CROSS JOIN generate_series(t1.start_date,t1.end_date,cast('1 day' as interval)) dt\n" +
            "group by t1.id)\n" +
            "as days\n" +
            "INNER JOIN \n" +
            "(SELECT result2.task_id,client_id, employee_id, employee.username from\n" +
            "(SELECT result.task_id, task_name, task_start_date, result.task_end_date,project_id,project_name,client_id, employee_id FROM\n" +
            "(SELECT task.id as task_id, task.name as task_name, task.start_date as task_start_date, task.end_date as task_end_date, project.id as project_id,  \n" +
            "project.name as project_name , project.client_id\n" +
            "FROM project,task\n" +
            "WHERE project.id=task.project_id)\n" +
            "as result, employee_task\n" +
            "WHERE employee_task.task_id=result.task_id\n" +
            "AND client_id= :client_id)\n" +
            "as result2, employee\n" +
            "WHERE employee.id=result2.employee_id)\n" +
            "as info\n" +
            "ON days.task_id=info.task_id)\n" +
            "as final_table\n" +
            "GROUP BY employee_id",nativeQuery = true)
    List<WorkdaysRepartition> getWorkDaysPerClient(@Param("client_id")long client_id);


    //List<Integer> getWorkDaysPerMonth(long employee_id, long client_id);
}

