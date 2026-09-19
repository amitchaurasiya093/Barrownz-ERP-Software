package com.erp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.ProjectDetails;
import com.erp.repository.ProjectDetailsRepository;
import com.erp.repository.TaskPriorityRepository;
import com.erp.repository.TaskStatusRepository;
import com.erp.repository.TaskSubjectRepository;
import com.erp.repository.TaskTypeRepository;
import com.erp.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/employee-dashboard")
@CrossOrigin("*")
public class EmployeeDashboardRestController {

    private TaskTypeRepository typeRepo;
    private TaskPriorityRepository priorityRepo;
    private TaskStatusRepository statusRepo;
    private TaskSubjectRepository subjectRepo;
    private ProjectDetailsRepository projectDetailsRepo;
    private JwtUtil jwtUtil;

    public EmployeeDashboardRestController(TaskTypeRepository typeRepo, TaskPriorityRepository priorityRepo,
            TaskStatusRepository statusRepo, TaskSubjectRepository subjectRepo,
            ProjectDetailsRepository projectDetailsRepo, JwtUtil jwtUtil) {
        this.typeRepo = typeRepo;
        this.priorityRepo = priorityRepo;
        this.statusRepo = statusRepo;
        this.subjectRepo = subjectRepo;
        this.projectDetailsRepo = projectDetailsRepo;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/dropdowns")
    public ResponseEntity<Map<String, Object>> getDropdownsForEmployee(HttpServletRequest request) {
        int loggedInEmpId = jwtUtil.extractEmpIdFromRequest(request);

        Map<String, Object> response = new HashMap<>();
        response.put("types", typeRepo.findAll());
        response.put("priorities", priorityRepo.findAll());
        response.put("statuses", statusRepo.findAll());
        response.put("subjects", subjectRepo.findAll());

        // filtering projects based on the logged in employee which is assigned to the

        List<ProjectDetails> allProjects = projectDetailsRepo.findAll();
        List<Map<String, Object>> filteredProjects = allProjects.stream()
                .filter(project -> project.getTeamMembers().stream()
                        .anyMatch(emp -> emp.getEmpId() == loggedInEmpId))
                .map(project -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("projectId", project.getProjectName().getProjectId());
                    map.put("projectName", project.getProjectName().getProjectName());
                    return map;
                })
                .distinct()
                .collect(Collectors.toList());

        response.put("projects", filteredProjects);

        return ResponseEntity.ok(response);

    }

}
