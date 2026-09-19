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

import com.erp.model.TaskPriority;
import com.erp.service.TaskPriorityService;

@RestController
@RequestMapping("/api/priority")
@CrossOrigin("*")
public class TaskPriorityRestController {

    @Autowired
    private TaskPriorityService priorityService;

    // handler for creating task project
    @PostMapping
    public ResponseEntity<TaskPriority> createTaskPriority(@RequestBody TaskPriority taskPriority) {
        TaskPriority tp = priorityService.saveTaskPriority(taskPriority);
        return new ResponseEntity<>(tp, HttpStatus.CREATED);
    }

    // handler for showing All Task Projects

    @GetMapping
    public ResponseEntity<List<TaskPriority>> getAllPriority() {
        List<TaskPriority> allPriority = priorityService.getAllTaskPriority();

        return new ResponseEntity<>(allPriority, HttpStatus.OK);
    }

    // handler for get single project

    @GetMapping("/{id}")
    public ResponseEntity<TaskPriority> getSinglePriority(@PathVariable int id) {
        TaskPriority singlePriority = priorityService.getSingleTaskPriority(id);
        return new ResponseEntity<>(singlePriority, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskPriority> updateTaskPriority(@PathVariable int id, @RequestBody TaskPriority priority) {
        TaskPriority updatedPriority = priorityService.updateTaskPriority(id, priority);
        return new ResponseEntity<>(updatedPriority, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskPriority(@PathVariable int id) {
        priorityService.deleteTaskPriority(id);
        return new ResponseEntity<>("proejct deleted successfully !", HttpStatus.GONE);
    }

}
