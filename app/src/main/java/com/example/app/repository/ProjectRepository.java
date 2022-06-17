package com.example.app.repository;

import com.example.app.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findById(long id);
    //long countByStart_date(LocalDate date);

    @Query(value="select count(client_id) as clients, count(projects) as projects from\n" +
            "(SELECT client_id, count(id) as projects FROM project\n" +
            "where project.status != 'finished'\n" +
            "group by client_id)\n" +
            "as t1",nativeQuery = true)
    List<List<Integer>> countCurrentProjectsAndClients();


    @Query(value="select project_id,cl.name as client_name,p.name as project_name,client_id,address,country,mail,phonenumber,cost,codetva from\n" +
            "project as p\n" +
            "inner join client as cl on p.client_id=cl.id\n" +
            "inner join contract as cn on cn.project_id=p.id\n" +
            "where client_id= :client_id\n" +
            "and project_id= :project_id",nativeQuery = true)
    Invoice getInvoiceInfo(@Param("client_id") long client_id, @Param("project_id") long project_id);

   /* @Query(" SELECT * FROM\n" +
            "(SELECT project.id as project_id,client.id as client_id, contract.id as contract_id ,\n" +
            "project.name as project_name, client.name as client_name, contract.cost as contract_cost \n" +
            "FROM project, client, contract\n" +
            "WHERE  project.client_id=client.id \n" +
            "AND project.id=contract.project_id\n" +
            "\n" +
            ")\n" +
            "as t1\n" +
            "WHERE t1.client_id= :id")
    List<Project> fetchProjectsByClientId(@Param("id")long id);*/

    @Query(value="select first_name as project_owner_first_name,last_name as project_owner_last_name,project.id as project_id, project.name as project_name,\n" +
            "client_id, project_owner,team_id, client.address as client_address,\n" +
            "country as client_country, mail as client_mail,\n" +
            "client.name as client_name, phonenumber as client_phone,\n" +
            "codetva\n" +
            "from project\n" +
            "inner join client\n" +
            "on client.id=client_id\n" +
            "left join contract\n" +
            "on contract.project_id=project.id\n" +
            "inner join employee\n" +
            "on employee.id=project.project_owner",nativeQuery = true)
    List<ClientData> getAllClientsAndProjects();
}
