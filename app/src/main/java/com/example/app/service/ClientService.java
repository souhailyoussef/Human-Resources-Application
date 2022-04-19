package com.example.app.service;

import com.example.app.domain.AppUser;
import com.example.app.domain.Client;
import com.example.app.domain.ClientDetails;
import com.example.app.domain.Project;

import java.util.List;

public interface ClientService {
    Client saveClient(Client client);
    Client getClient(long id);
    List<Client> getClients();
    List<ClientDetails> fetchClientDetails(long id);
    List<Project> fetchProjectsByClientId(long id);
    long countClients();
}
