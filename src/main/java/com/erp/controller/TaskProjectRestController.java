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

import com.erp.model.TaskProject;
import com.erp.service.TaskProjectService;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin("*")
public class TaskProjectRestController {

    @Autowired
    private TaskProjectService projectService;

    // handler for creating task project
    @PostMapping
    public ResponseEntity<TaskProject> createTaskProject(@RequestBody TaskProject taskProject) {
        TaskProject tp = projectService.saveTaskProject(taskProject);
        return new ResponseEntity<>(tp, HttpStatus.CREATED);
    }

    // handler for showing All Task Projects

    @GetMapping
    public ResponseEntity<List<TaskProject>> getAllProjects() {
        List<TaskProject> allProjects = projectService.getAllTaskProjects();

        return new ResponseEntity<>(allProjects, HttpStatus.OK);
    }

    // handler for get single project

    @GetMapping("/{id}")
    public ResponseEntity<TaskProject> getSingleProject(@PathVariable int id) {
        TaskProject singleProject = projectService.getSingleTaskProject(id);
        return new ResponseEntity<>(singleProject, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskProject> updateTaskProject(@PathVariable int id, @RequestBody TaskProject project) {
        TaskProject updatedProject = projectService.updateTaskProject(id, project);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskProject(@PathVariable int id) {
        projectService.deleteTaskProject(id);
        return new ResponseEntity<>("proejct deleted successfully !", HttpStatus.GONE);
    }
}
