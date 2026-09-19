package com.erp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.Employee;
import com.erp.model.LeaveApplication;
import com.erp.model.LeaveBalance;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LeaveApplicationRepository;
import com.erp.repository.LeaveBalanceRepository;
import com.erp.service.AdminLeaveService;
import com.erp.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin/leaves")
public class AdminLeaveRestController {

    private final AdminLeaveService adminLeaveService;
    private final JwtUtil jwtUtil;

    public AdminLeaveRestController(AdminLeaveService adminLeaveService, JwtUtil jwtUtil) {
        this.adminLeaveService = adminLeaveService;
        this.jwtUtil = jwtUtil;
    }

    @PutMapping("/{leaveId}/status/{status}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<?> updateLeaveStatus(@PathVariable int leaveId,
            @PathVariable String status,
            HttpServletRequest request) {
        try {
            int adminEmpId = jwtUtil.extractEmpIdFromRequest(request);
            String result = adminLeaveService.updateLeaveStatus(leaveId, status, adminEmpId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
