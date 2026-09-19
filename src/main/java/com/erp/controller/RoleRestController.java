package com.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.Role;
import com.erp.repository.RoleRepository;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin("*")
public class RoleRestController {

    @Autowired
    private RoleRepository roleRepo;

    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
}
