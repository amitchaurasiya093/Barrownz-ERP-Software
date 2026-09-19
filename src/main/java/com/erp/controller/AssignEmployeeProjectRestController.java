package com.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.AssignEmployeeProject;
import com.erp.model.Employee;
import com.erp.repository.EmployeeRepository;
import com.erp.service.AssignEmployeeProjectService;

@RestController
@RequestMapping("/api/employee-projects")
@CrossOrigin("*")
public class AssignEmployeeProjectRestController {

    @Autowired
    private AssignEmployeeProjectService service;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public List<AssignEmployeeProject> getAllAssignments() {
        return service.getAll();
    }

    @GetMapping("/employee/{empId}")
    public List<AssignEmployeeProject> getByEmployeeId(@PathVariable int empId) {
        return service.getByEmployeeId(empId);
    }

    @PostMapping
    public List<AssignEmployeeProject> assignProjects(@RequestBody List<AssignEmployeeProject> list) {
        return list.stream()
                .map(assignment -> {
                    if (assignment.getEmployee() != null) {
                        int empId = assignment.getEmployee().getEmpId();
                        Employee emp = employeeRepository.findById(empId).orElse(null);
                        assignment.setEmployee(emp);
                    }
                    return service.save(assignment);
                })
                .toList();
    }

    @PutMapping("/{id}")
    public AssignEmployeeProject update(@PathVariable int id, @RequestBody AssignEmployeeProject updated) {
        if (updated.getEmployee() != null) {
            int empId = updated.getEmployee().getEmpId();
            Employee emp = employeeRepository.findById(empId).orElse(null);
            updated.setEmployee(emp);
        }
        return service.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

}
