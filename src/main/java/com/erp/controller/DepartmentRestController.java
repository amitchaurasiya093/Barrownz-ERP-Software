package com.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.erp.model.Department;
import com.erp.service.DepartmentService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentRestController {

    @Autowired
    private DepartmentService departmentService;

    // handler for savng department
    @PostMapping
    public ResponseEntity<Department> saveDepartment(@RequestBody Department department) {
        Department dept = departmentService.createDepartment(department);
        return new ResponseEntity<>(dept, HttpStatus.CREATED);
    }

    // handler for getall departments
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> allDepartments = departmentService.getAllDepartments();
        return new ResponseEntity<>(allDepartments, HttpStatus.OK);
    }

    // handler for sngle department
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable int id) {
        Department department = departmentService.getDepartmentById(id);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    // update Department Handler
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int id, @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
    }

    // delete department handler
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>("department deleted successfully", HttpStatus.GONE);
    }

}
