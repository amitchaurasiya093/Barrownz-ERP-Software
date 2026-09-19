package com.erp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.LeaveBalance;
import com.erp.repository.LeaveBalanceRepository;

@RestController
@RequestMapping("/api/leave-balances")
public class LeaveBalanceRestController {

    private LeaveBalanceRepository leaveBalanceRepo;

    public LeaveBalanceRestController(LeaveBalanceRepository leaveBalanceRepo) {
        this.leaveBalanceRepo = leaveBalanceRepo;
    }

    @GetMapping("/{empId}")
    public ResponseEntity<List<LeaveBalance>> getBalances(@PathVariable int empId) {
        List<LeaveBalance> balances = leaveBalanceRepo.findByEmployeeEmpId(empId);
        return new ResponseEntity<>(balances, HttpStatus.OK);
    }

}
