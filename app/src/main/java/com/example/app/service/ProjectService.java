package com.example.app.service;

import com.example.app.domain.ClientData;
import com.example.app.domain.Invoice;
import com.example.app.domain.Project;
import com.example.app.domain.Task;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {
    Project saveProject(Project project);
    Project getProject(long id);
    List<Project> getProjects();
   // Project addTask(Task task, long project_id);
    long countProjects(LocalDate date);
    List<List<Integer>> countCurrentProjectsAndClients();
    Invoice getInvoiceInfo(long client_id, long project_id);

    List<ClientData>  getAllClientsAndProjects();
}
