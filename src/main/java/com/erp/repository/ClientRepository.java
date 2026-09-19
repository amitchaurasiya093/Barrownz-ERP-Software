package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
