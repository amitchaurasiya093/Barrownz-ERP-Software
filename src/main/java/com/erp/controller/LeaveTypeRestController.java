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

import com.erp.model.LeaveType;
import com.erp.repository.LeaveTypeRepository;
import com.erp.service.LeaveTypeService;

@RestController
@RequestMapping("/api/leavetypes")
@CrossOrigin(origins = "*")
public class LeaveTypeRestController {

    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveTypeService leaveTypeService;

    LeaveTypeRestController(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    
    @PostMapping
    public ResponseEntity<LeaveType> saveLeaveType(@RequestBody LeaveType leavetype) {
        LeaveType l_type = leaveTypeService.createLeaveType(leavetype);
        return new ResponseEntity<>(l_type, HttpStatus.CREATED);
    }

    // handler for getall leave types
    @GetMapping
    public ResponseEntity<List<LeaveType>> getAllLeaveTypes() {
        List<LeaveType> allLeaveTypes = leaveTypeService.getAllLeavetypes();
        return new ResponseEntity<>(allLeaveTypes, HttpStatus.OK);
    }

    // handler for single leave type
    @GetMapping("/{id}")
    public ResponseEntity<LeaveType> getLeaveType(@PathVariable int id) {
        LeaveType leaveType = leaveTypeService.getLeaveTypeById(id);
        return new ResponseEntity<>(leaveType, HttpStatus.OK);
    }

    // update Department Handler
    @PutMapping("/{id}")
    public ResponseEntity<LeaveType> updateLeaveType(@PathVariable int id, @RequestBody LeaveType leavetype) {
        LeaveType updatedLeaveType = leaveTypeService.updateLeaveType(id, leavetype);
        return new ResponseEntity<>(updatedLeaveType, HttpStatus.OK);
    }

    // delete department handler
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeaveType(@PathVariable int id) {
        leaveTypeService.deleteLeaveType(id);
        return new ResponseEntity<>("LeaveType deleted successfully", HttpStatus.GONE);
    }

}
