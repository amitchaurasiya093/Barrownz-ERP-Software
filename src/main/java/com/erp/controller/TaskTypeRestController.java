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

import com.erp.model.TaskType;
import com.erp.service.TaskTypeService;

@RestController
@RequestMapping("/api/types")
@CrossOrigin("*")
public class TaskTypeRestController {

    @Autowired
    private TaskTypeService typeService;

    // handler for create type

    @PostMapping
    public ResponseEntity<TaskType> createTaskType(@RequestBody TaskType type) {
        TaskType taskType = typeService.saveTaskType(type);
        return new ResponseEntity<>(taskType, HttpStatus.CREATED);
    }

    // handler for showAll

    @GetMapping
    public ResponseEntity<List<TaskType>> showAllTypes() {
        List<TaskType> alltypes = typeService.getAllTaskTypes();
        return new ResponseEntity<>(alltypes, HttpStatus.OK);
    }

    // handler for get single

    @GetMapping("/{id}")
    public ResponseEntity<TaskType> getSingleType(@PathVariable int id) {
        TaskType singleType = typeService.getSingleTaskType(id);
        return new ResponseEntity<>(singleType, HttpStatus.OK);
    }

    // handler for update Types

    @PutMapping("/{id}")
    public ResponseEntity<TaskType> updateType(@PathVariable int id, @RequestBody TaskType tasktypes) {
        TaskType updatedType = typeService.updateTaskType(id, tasktypes);
        return new ResponseEntity<>(updatedType, HttpStatus.OK);
    }

    // handler for delete type

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeType(@PathVariable int id) {
        typeService.deleteTaskType(id);
        return new ResponseEntity<>("type deleted successfully !", HttpStatus.GONE);
    }
}
