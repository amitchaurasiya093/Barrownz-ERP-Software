package com.erp.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.LeaveApplicationDto;
import com.erp.model.Employee;
import com.erp.model.LeaveApplication;
import com.erp.repository.EmployeeRepository;
import com.erp.service.LeaveApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin("*")
public class LeaveApplicationRestController {

    private LeaveApplicationService leaveService;
    private EmployeeRepository employeeRepo;

    public LeaveApplicationRestController(LeaveApplicationService leaveService, EmployeeRepository employeeRepo) {
        this.leaveService = leaveService;
        this.employeeRepo = employeeRepo;
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<LeaveApplication> applyLeave(
            @RequestPart("leaveDto") String leaveDtoJson,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws IllegalArgumentException, JsonMappingException, JsonProcessingException {

        // convert JSON string to DTO
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        LeaveApplicationDto dto = mapper.readValue(leaveDtoJson, LeaveApplicationDto.class);

        String attachmentPath = null;
        try {
            if (file != null && !file.isEmpty()) {
                attachmentPath = leaveService.saveAttachment(file);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        LeaveApplication leave = leaveService.applyLeave(dto, attachmentPath);
        return new ResponseEntity<>(leave, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveApplicationDto>> getLeavesByEmployee(@PathVariable int employeeId) {
        return ResponseEntity.ok(leaveService.getLeavesByEmployee(employeeId));
    }

    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeaveApplicationDto>> getMyLeaves(Principal principal) {
        // principal.getName() = empCode (because login is with empCode)
        int empCode = Integer.parseInt(principal.getName());

        Employee emp = employeeRepo.findByEmpCode(empCode);
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<LeaveApplicationDto> leaves = leaveService.getLeavesByEmployee(emp.getEmpId());
        return ResponseEntity.ok(leaves);
    }

    // handler for get monthly leaves
    @GetMapping("/employees/{empId}/monthlyLeave")
    public ResponseEntity<Map<String, Object>> getMonthlyLeave(
            @PathVariable int empId,
            @RequestParam int month,
            @RequestParam int year) {

        Double total = leaveService.getMonthlyLeaveTotal(empId, month, year);

        Map<String, Object> response = new HashMap<>();
        response.put("employeeId", empId);
        response.put("month", month);
        response.put("year", year);
        response.put("totalLeave", total);

        return ResponseEntity.ok(response);
    }

}
