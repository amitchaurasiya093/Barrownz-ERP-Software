package com.erp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.EmployeePersonalnfoDto;
import com.erp.model.Employee;
import com.erp.model.EmployeePersonalInfo;
import com.erp.repository.EmployeeRepository;
import com.erp.service.EmployeePersonalInfoService;

@RestController
@RequestMapping("/api/personal-info")
@CrossOrigin("*")
public class EmployeePersonalInfoRestController {

    private EmployeePersonalInfoService infoService;
    private EmployeeRepository employeeRepo;

    public EmployeePersonalInfoRestController(EmployeePersonalInfoService infoService,
            EmployeeRepository employeeRepo) {
        this.infoService = infoService;
        this.employeeRepo = employeeRepo;
    }

    // @PostMapping
    // public ResponseEntity<EmployeePersonalInfo> createPersonalInfo(@RequestBody
    // EmployeePersonalnfoDto dto) {

    // if (dto.getEmployeeId() == null) {
    // throw new IllegalArgumentException("Employee id cannot be null");
    // }

    // Employee emp = employeeRepo.findById(dto.getEmployeeId())
    // .orElseThrow(() -> new RuntimeException("Employee not found"));

    // EmployeePersonalInfo info = new EmployeePersonalInfo();
    // info.setHeight(dto.getHeight());
    // info.setWeight(dto.getWeight());
    // info.setPassportNumber(dto.getPassportNumber());
    // info.setPanNumber(dto.getPanNumber());
    // info.setAadharNumber(dto.getAadharNumber());
    // info.setReligion(dto.getReligion());
    // info.setMaritalStatus(dto.getMaritalStatus());
    // info.setBloodGroup(dto.getBloodGroup());
    // info.setShift(dto.getShift());
    // info.setEmployee(emp);

    // EmployeePersonalInfo savedInfo = infoService.savePersonalInfo(info);

    // return ResponseEntity.ok(savedInfo);
    // }

    // handler for creating new or existing info of a employee (save/update)

    @PostMapping
    public ResponseEntity<EmployeePersonalInfo> saveOrUpdatePersonalInfo(@RequestBody EmployeePersonalnfoDto dto) {

        if (dto.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee Id cannot be null");
        }

        // verifying if employee exists
        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId()));

        // find existing or create new
        EmployeePersonalInfo info = infoService.findByEmployee(employee).orElse(new EmployeePersonalInfo());

        // update all fields
        info.setHeight(dto.getHeight());
        info.setWeight(dto.getWeight());
        info.setPassportNumber(dto.getPassportNumber());
        info.setPanNumber(dto.getPanNumber());
        info.setAadharNumber(dto.getAadharNumber());
        info.setReligion(dto.getReligion());
        info.setMaritalStatus(dto.getMaritalStatus());
        info.setBloodGroup(dto.getBloodGroup());
        info.setShift(dto.getShift());
        info.setEmployee(employee);

        // save and return
        EmployeePersonalInfo savedInfo = infoService.savePersonalInfo(info);
        return new ResponseEntity<>(savedInfo, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeePersonalInfo>> getAll() {
        List<EmployeePersonalInfo> allInfos = infoService.findAllInfo();
        return new ResponseEntity<>(allInfos, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Optional<EmployeePersonalInfo>> getByEmployeeId(@PathVariable int employeeId) {

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("employee not found: " + employeeId));

        Optional<EmployeePersonalInfo> empInfo = infoService.findByEmployee(employee);

        return new ResponseEntity<>(empInfo, HttpStatus.OK);
    }

}
