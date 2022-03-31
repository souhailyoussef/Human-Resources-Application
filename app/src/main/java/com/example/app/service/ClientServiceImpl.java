package com.example.app.service;

import com.example.app.domain.Client;
import com.example.app.domain.ClientDetails;
import com.example.app.repository.ClientRepository;
import com.example.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    @Override
    public Client saveClient(Client client) {
        log.info("saving client : {}", client);
        return clientRepository.save(client);
    }

    @Override
    public Client getClient(long id) {
        log.info("fetching client by id : {}", id);
        return clientRepository.findById(id);
    }

    @Override
    public List<Client> getClients() {
        log.info("fetching all clients");
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDetails> fetchClientDetails(long id) {
        return clientRepository.fetchClientDetails(id);
    }
}
