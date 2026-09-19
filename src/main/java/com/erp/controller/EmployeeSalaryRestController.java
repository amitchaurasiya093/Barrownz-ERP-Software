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

import com.erp.dto.EmployeeSalaryDto;
import com.erp.model.EmployeeSalary;
import com.erp.service.EmployeeSalaryService;

@RestController
@RequestMapping("/api/salary")
@CrossOrigin("*")
public class EmployeeSalaryRestController {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    // handler for creating salary
    @PostMapping
    public ResponseEntity<EmployeeSalaryDto> createEmployeeSalary(@RequestBody EmployeeSalaryDto dto) {
        EmployeeSalaryDto savedSalary = employeeSalaryService.saveEmployeeSalary(dto);
        return new ResponseEntity<>(savedSalary, HttpStatus.CREATED);
    }

    // handler for getting All Salaries

    @GetMapping
    public ResponseEntity<List<EmployeeSalaryDto>> getAllSalaries() {
        List<EmployeeSalaryDto> allSalaries = employeeSalaryService.getAllSalary();
        return new ResponseEntity<>(allSalaries, HttpStatus.OK);
    }

    // handler for get single Salary

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeSalaryDto> getSingleSalary(@PathVariable int id) {
        EmployeeSalaryDto singleSalary = employeeSalaryService.getSingleSalary(id);
        return new ResponseEntity<>(singleSalary, HttpStatus.OK);
    }

    // handler for update salary

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeSalaryDto> updateSalary(@PathVariable int id,
            @RequestBody EmployeeSalaryDto empSalary) {
        EmployeeSalaryDto updatedSalary = employeeSalaryService.updateSalary(id, empSalary);
        return new ResponseEntity<>(updatedSalary, HttpStatus.OK);
    }

    // handler for delete salary

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalary(@PathVariable int id) {
        employeeSalaryService.deleteSalary(id);
        return new ResponseEntity<>("salary deleted successfully !", HttpStatus.GONE);
    }

    // Handler for get Salary By employee Id

    @GetMapping("/by-employee/{empId}")
    public ResponseEntity<EmployeeSalaryDto> getEmployeeSalaryByEmployeeId(@PathVariable int empId) {
        EmployeeSalaryDto salary = employeeSalaryService.getSalaryByEmployeeId(empId);
        if (salary != null) {
            return new ResponseEntity<>(salary, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
