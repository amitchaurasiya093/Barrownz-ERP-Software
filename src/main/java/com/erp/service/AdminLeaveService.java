package com.erp.service;

import org.springframework.stereotype.Service;

import com.erp.model.Employee;
import com.erp.model.LeaveApplication;
import com.erp.model.LeaveBalance;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LeaveApplicationRepository;
import com.erp.repository.LeaveBalanceRepository;

@Service
public class AdminLeaveService {

    private final LeaveApplicationRepository leaveRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final EmployeeRepository employeeRepo;
    private final EmailService emailService;

    public AdminLeaveService(LeaveApplicationRepository leaveRepo, LeaveBalanceRepository leaveBalanceRepo,
            EmployeeRepository employeeRepo, EmailService emailService) {
        this.leaveRepo = leaveRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.employeeRepo = employeeRepo;
        this.emailService = emailService;
    }

    public String updateLeaveStatus(int leaveId, String status, int adminEmpId) {
        LeaveApplication leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!status.equalsIgnoreCase("APPROVED") &&
                !status.equalsIgnoreCase("REJECTED") &&
                !status.equalsIgnoreCase("PENDING")) {
            throw new RuntimeException("Invalid status");
        }

        Employee admin = employeeRepo.findById(adminEmpId)
                .orElseThrow(() -> new RuntimeException("Admin employee not found"));

        String previousStatus = leave.getStatus();
        double leaveQty = leave.getLeaveQuantity();

        // adjust balance only if leave type requires balance
        if (Boolean.TRUE.equals(leave.getLeaveType().getRequiresBalance())) {
            LeaveBalance balance = leaveBalanceRepo.findByEmployeeEmpIdAndLeaveTypeLeavetypeId(
                    leave.getEmployee().getEmpId(),
                    leave.getLeaveType().getLeavetypeId())
                    .orElseThrow(() -> new RuntimeException("Leave balance not found"));

            // case 1: Admin approves the leave that was previously rejected or pending
            if ("APPROVED".equalsIgnoreCase(status) && !"APPROVED".equalsIgnoreCase(previousStatus)) {
                double newBalance = balance.getBalance() - leaveQty;
                if (newBalance < 0) {
                    throw new RuntimeException("Insufficient leave balance at admin approval");
                }
                balance.setBalance(newBalance);
                leaveBalanceRepo.save(balance);
            }

            // case 2: if admin rejects a leave that was previously approved
            if ("REJECTED".equalsIgnoreCase(status) && "APPROVED".equalsIgnoreCase(previousStatus)) {
                balance.setBalance(balance.getBalance() + leaveQty);
                leaveBalanceRepo.save(balance);
            }
            // case 3: pending status changes
            if ("PENDING".equalsIgnoreCase(status)) {
                // if previously approved restore the balance
                if ("APPROVED".equalsIgnoreCase(previousStatus)) {
                    balance.setBalance(balance.getBalance() + leaveQty);
                    leaveBalanceRepo.save(balance);
                }
            }

        }

        leave.setStatus(status.toUpperCase());
        leave.setActionBy(admin);
        leaveRepo.save(leave);

        // send mail
        emailService.sendLeaveStatusUpdateEmail(leave);

        return "Leave status updated to " + status.toUpperCase() + " by " + admin.getFirstName() + " "
                + admin.getLastName();

    }

}
