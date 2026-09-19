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

import com.erp.model.ReportingManager;
import com.erp.service.ReportingManagerService;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin("*")
public class ReportingManagerRestController {

    @Autowired
    private ReportingManagerService rmService;

    // handler for saving RM
    @PostMapping
    public ResponseEntity<ReportingManager> createReportingManager(@RequestBody ReportingManager rm) {

        ReportingManager manager = rmService.createReportingManager(rm);
        return new ResponseEntity<>(manager, HttpStatus.CREATED);
    }

    // handler for showing RMs

    @GetMapping
    public ResponseEntity<List<ReportingManager>> showReportingManagers() {

        List<ReportingManager> allManagers = rmService.getAllReportingManagers();
        return new ResponseEntity<>(allManagers, HttpStatus.OK);
    }

    // handler for show single RM

    @GetMapping("/{id}")
    public ResponseEntity<ReportingManager> showSingleManager(@PathVariable int id) {
        ReportingManager rm = rmService.getReportingManager(id);

        return new ResponseEntity<>(rm, HttpStatus.OK);

    }

    // handler for update RM

    @PutMapping("/{id}")
    public ResponseEntity<ReportingManager> updateReportingManager(@PathVariable int id,
            @RequestBody ReportingManager rm) {

        ReportingManager updatedManager = rmService.updateReportingManager(rm, id);
        return new ResponseEntity<>(updatedManager, HttpStatus.OK);
    }

    // handler for delete RM

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReportingManager(@PathVariable int id) {
        rmService.deleteReportingManager(id);
        return new ResponseEntity<>("reporting manager deleted successfully", HttpStatus.GONE);
    }

    // handler for getting all reporting managers by department
    // like if admin select java then it should show all reporting managers of java
    // department

    @GetMapping("/by-department/{id}")
    public ResponseEntity<List<ReportingManager>> showAllManagersByDepartmentId(@PathVariable int id) {
        List<ReportingManager> rmManagers = rmService.getReportingManagersByDepartmentId(id);
        return new ResponseEntity<>(rmManagers, HttpStatus.OK);
    }

}
