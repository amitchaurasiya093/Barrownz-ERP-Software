package com.erp.service;

import java.util.List;

import com.erp.model.Client;

public interface ClientService {

    public Client saveClient(Client client);

    public List<Client> showAllClients();

    public void deleteClient(int id);

    public Client updateClient(int id, Client client);

    public Client getSingleClientById(int id);
}
