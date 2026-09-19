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

import com.erp.model.TaskSubject;
import com.erp.service.TaskSubjectService;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin("*")
public class TaskSubjectRestController {

    @Autowired
    private TaskSubjectService subjectService;

    // handler for adding Task

    @PostMapping
    public ResponseEntity<TaskSubject> createTaskSubject(@RequestBody TaskSubject subject) {
        TaskSubject tsubject = subjectService.saveTaskSubject(subject);
        return new ResponseEntity<>(tsubject, HttpStatus.CREATED);
    }

    // handler for showing all

    @GetMapping
    public ResponseEntity<List<TaskSubject>> showAllSubjects() {
        List<TaskSubject> allSubjects = subjectService.getAllTaskSubjects();
        return new ResponseEntity<>(allSubjects, HttpStatus.OK);
    }

    // handler for single subject
    @GetMapping("/{id}")
    public ResponseEntity<TaskSubject> getSingleSubject(@PathVariable int id) {
        TaskSubject singleSubject = subjectService.getSingleTaskSubject(id);
        return new ResponseEntity<>(singleSubject, HttpStatus.OK);
    }

    // update subject handler

    @PutMapping("/{id}")
    public ResponseEntity<TaskSubject> updateSubject(@PathVariable int id, @RequestBody TaskSubject subject) {
        TaskSubject updatedSubject = subjectService.updateTaskSubject(id, subject);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
    }

    // handler for delete

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubject(@PathVariable int id) {
        subjectService.deleteTaskSubject(id);
        return new ResponseEntity<>("subject deleted successfully !", HttpStatus.GONE);
    }

}
