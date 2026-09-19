package com.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.Client;
import com.erp.service.ClientService;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    // handler for saving client

    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        Client newClient = clientService.saveClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    // handler for showing all clients

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> allClients = clientService.showAllClients();
        return new ResponseEntity<>(allClients, HttpStatus.OK);
    }

    // handler for get single Client

    @GetMapping("/{id}")
    public ResponseEntity<Client> getSingleClient(@PathVariable int id) {
        Client singleClient = clientService.getSingleClientById(id);
        return new ResponseEntity<>(singleClient, HttpStatus.OK);
    }

    // handler for updating Clients

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable int id, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(id, client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    // handler for deleting clients

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeClient(@PathVariable int id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>("client deleted successfully !", HttpStatus.GONE);
    }

}
