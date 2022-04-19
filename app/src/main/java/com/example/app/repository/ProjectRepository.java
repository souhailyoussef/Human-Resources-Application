package com.example.app.repository;

import com.example.app.domain.Client;
import com.example.app.domain.FileDB;
import com.example.app.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Project findById(long id);
    //long countByStart_date(LocalDate date);

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
}
