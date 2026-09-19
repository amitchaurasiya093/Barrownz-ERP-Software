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

import com.erp.model.TaskStatus;
import com.erp.service.TaskStatusService;

@RestController
@RequestMapping("/api/status")
@CrossOrigin("*")
public class TaskStatusRestController {

    @Autowired
    private TaskStatusService statusService;

    @PostMapping
    public ResponseEntity<TaskStatus> createTaskStatus(@RequestBody TaskStatus taskStatus) {
        TaskStatus ts = statusService.saveTaskStatus(taskStatus);
        return new ResponseEntity<>(ts, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskStatus>> getAllStatus() {
        List<TaskStatus> allStatus = statusService.getAllTaskStatus();
        return new ResponseEntity<>(allStatus, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatus> getSingleStatus(@PathVariable int id) {
        TaskStatus singleStatus = statusService.getSingleTaskStatus(id);
        return new ResponseEntity<>(singleStatus, HttpStatus.OK);
    }

    // Update Task Status
    @PutMapping("/{id}")
    public ResponseEntity<TaskStatus> updateTaskStatus(@PathVariable int id, @RequestBody TaskStatus status) {
        TaskStatus updatedStatus = statusService.updateTaskStatus(id, status);
        return new ResponseEntity<>(updatedStatus, HttpStatus.OK);
    }

    // Delete Task Status
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskStatus(@PathVariable int id) {
        statusService.deleteTaskStatus(id);
        return new ResponseEntity<>("Task status deleted successfully!", HttpStatus.OK);
    }

}
