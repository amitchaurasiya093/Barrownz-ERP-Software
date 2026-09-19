package com.erp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.erp.dto.ProjectDetailsRequestDto;
import com.erp.model.Employee;
import com.erp.model.ProjectDetails;
import com.erp.service.ProjectDetailsService;

@RestController
@RequestMapping("/api/projectDetails")
@CrossOrigin("*")
public class ProjectDetailsController {

    @Autowired
    private ProjectDetailsService service;

    // Create
    @PostMapping
    public ResponseEntity<String> createProject(@RequestBody ProjectDetailsRequestDto dto) {
        try {
            String result = service.createProject(dto);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Error: " + e.getMessage());
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable int id, @RequestBody ProjectDetailsRequestDto dto) {
        try {
            String result = service.updateProject(id, dto);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Error: " + e.getMessage());
        }
    }

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable int id) {
        try {
            ProjectDetails project = service.getProjectById(id);
            return ResponseEntity.ok(project);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
        }
    }

    // Get all
    @GetMapping
    public ResponseEntity<List<ProjectDetailsRequestDto>> getAllProjects() {
        List<ProjectDetailsRequestDto> list = service.getAllProjectDetails();
        return ResponseEntity.ok(list);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable int id) {
        try {
            String result = service.deleteProjectDetails(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/calculate-cost")
    public ResponseEntity<?> calculateCost(@RequestBody ProjectDetailsRequestDto dto) {
        try {
            ProjectDetails temp = new ProjectDetails();
            List<Employee> team = service.buildTeamAndCalculate(dto, temp);

            Map<String, Double> response = new HashMap<>();
            response.put("rateCalculation", temp.getRateCalculation());
            response.put("manPerHour", temp.getManPerHour());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "❌ Error: " + e.getMessage()));
        }
    }

}
