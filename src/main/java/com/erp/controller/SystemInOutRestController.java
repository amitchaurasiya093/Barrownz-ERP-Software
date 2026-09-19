package com.erp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.SystemInOutDto;
import com.erp.service.SystemInOutService;

@RestController
@RequestMapping("/api/system")
@CrossOrigin("*")
public class SystemInOutRestController {

    private SystemInOutService systemService;

    public SystemInOutRestController(SystemInOutService systemService) {
        this.systemService = systemService;
    }

    // marks system in handler

    @PostMapping("/in/{employeeId}")
    public ResponseEntity<SystemInOutDto> markSystemIn(@PathVariable int employeeId) {
        SystemInOutDto systemIn = systemService.marksSystemIn(employeeId);
        return new ResponseEntity<>(systemIn, HttpStatus.OK);

    }

    // @PostMapping("/out/{employeeId}")
    // public ResponseEntity<SystemInOut> markSystemOut(@PathVariable int
    // employeeId) {
    // return ResponseEntity.ok(systemService.marksSystemOut(employeeId));
    // }

    @PostMapping("/out/{employeeId}")
    public ResponseEntity<?> markSystemOut(@PathVariable int employeeId) {
        try {
            SystemInOutDto record = systemService.marksSystemOut(employeeId);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/status/{employeeId}")
    public ResponseEntity<String> getStatus(@PathVariable int employeeId) {
        return ResponseEntity.ok(systemService.getCurrentStatus(employeeId));
    }

}
