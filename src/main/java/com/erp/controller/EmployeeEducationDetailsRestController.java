package com.erp.controller;

import java.util.List;

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

import com.erp.dto.EmployeeEducationDetailsDto;
import com.erp.model.EmployeeEducationDetails;
import com.erp.repository.EmployeeRepository;
import com.erp.service.EmployeeEducationDetailsService;

@RestController
@RequestMapping("/api/education-details")
@CrossOrigin("*")
public class EmployeeEducationDetailsRestController {

    private final EmployeeEducationDetailsService detailsService;
    private final EmployeeRepository employeeRepo;

    public EmployeeEducationDetailsRestController(EmployeeEducationDetailsService detailsService,
            EmployeeRepository employeeRepo) {
        this.detailsService = detailsService;
        this.employeeRepo = employeeRepo;
    }

    @PostMapping
    public ResponseEntity<?> saveEducation(@RequestBody EmployeeEducationDetailsDto dto) {
        EmployeeEducationDetails saved = detailsService.saveEducation(dto);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEducation(@PathVariable int id, @RequestBody EmployeeEducationDetailsDto dto) {
        EmployeeEducationDetails updated = detailsService.updateEducation(id, dto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEducationByEmployee(@PathVariable int employeeId) {
        List<EmployeeEducationDetails> list = detailsService.findByEmployeeId(employeeId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEducation(@PathVariable int id) {
        detailsService.deleteEducation(id);
        return ResponseEntity.noContent().build();
    }

}
