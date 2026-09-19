package com.erp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.LeaveApplicationDto;
import com.erp.model.Employee;
import com.erp.repository.EmployeeRepository;
import com.erp.service.LeaveApplicationService;

@RestController
@RequestMapping("/api/rm/leaves")
public class RMRestController {

    private LeaveApplicationService leaveService;
    private EmployeeRepository employeeRepo;

    public RMRestController(LeaveApplicationService leaveService, EmployeeRepository employeeRepo) {
        this.leaveService = leaveService;
        this.employeeRepo = employeeRepo;
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> updateLeaveByRM(@PathVariable int id, @PathVariable String status, Principal principal) {
        // principal.getName() returns empCode as String (because login is based on
        // empCode)
        String empCodeStr = principal.getName();
        int empCode;

        try {
            empCode = Integer.parseInt(empCodeStr); // convert string -> int
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid empCode format");
        }

        Employee rm = employeeRepo.findByEmpCode(empCode);
        if (rm == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RM not found");
        }

        boolean updated = leaveService.updateLeaveStatusByRM(id, status, rm.getEmpId());

        if (!updated) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to approve/reject this leave");
        }

        return ResponseEntity.ok("Leave updated successfully by RM");
    }

    // rm handler
    @GetMapping("/leave-all")
    public ResponseEntity<?> getLeavesForRM(Principal principal) {
        String empCodeStr = principal.getName();
        int empCode = Integer.parseInt(empCodeStr);

        Employee rm = employeeRepo.findByEmpCode(empCode);

        if (rm == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RM not found");
        }
        List<LeaveApplicationDto> leaves = leaveService.getLeavesForRM(rm.getEmpId());
        return ResponseEntity.ok(leaves);
    }
}
