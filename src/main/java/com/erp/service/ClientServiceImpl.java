package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Client;
import com.erp.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepo;

    @Override
    public Client saveClient(Client client) {
        return clientRepo.save(client);
    }

    @Override
    public List<Client> showAllClients() {
        List<Client> allClients = clientRepo.findAll();
        return allClients;
    }

    @Override
    public Client getSingleClientById(int id) {
        Client c = clientRepo.findById(id).orElseThrow(() -> new RuntimeException("client id not found: " + id));
        return c;
    }

    @Override
    public void deleteClient(int id) {
        clientRepo.deleteById(id);
    }

    @Override
    public Client updateClient(int id, Client client) {
        Client existingClient = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("client id not found: " + id));

        existingClient.setClientName(client.getClientName());
        existingClient.setProject(client.getProject());

        Client updatedClient = clientRepo.save(existingClient);
        return updatedClient;

    }

}
