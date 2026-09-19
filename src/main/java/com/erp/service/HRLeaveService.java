package com.erp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.dto.LeaveApplicationHRDto;
import com.erp.model.Employee;
import com.erp.model.LeaveApplication;
import com.erp.model.LeaveBalance;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LeaveApplicationRepository;
import com.erp.repository.LeaveBalanceRepository;
import com.erp.util.LeaveCalculator;

@Service
public class HRLeaveService {

    private LeaveApplicationRepository leaveRepo;
    private LeaveBalanceRepository leaveBalanceRepo;
    private EmployeeRepository employeeRepo;
    private EmailService emailService;

    public HRLeaveService(LeaveApplicationRepository leaveRepo, LeaveBalanceRepository leaveBalanceRepo,
            EmployeeRepository employeeRepo, EmailService emailService) {
        this.leaveRepo = leaveRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.employeeRepo = employeeRepo;
        this.emailService = emailService;
    }

    public List<LeaveApplicationHRDto> getAllLeaves() {
        List<LeaveApplication> leaves = leaveRepo.findAll();

        return leaves.stream().map(leave -> {
            LeaveApplicationHRDto dto = new LeaveApplicationHRDto();

            dto.setLeaveApplicationId(leave.getLeaveApplicationId());
            dto.setEmployeeId(leave.getEmployee().getEmpId());
            dto.setEmployeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName());
            dto.setEmployeeCode(leave.getEmployee().getEmpCode());

            dto.setLeaveTypeId(leave.getLeaveType().getLeavetypeId());
            dto.setLeaveTypeName(leave.getLeaveType().getLeaveType());

            dto.setFromDate(leave.getFromDate());
            dto.setToDate(leave.getToDate());
            dto.setSessionFrom(leave.getSessionFrom());
            dto.setSessionTo(leave.getSessionTo());
            dto.setContactDetails(leave.getContactDetails());
            dto.setReason(leave.getReason());
            dto.setStatus(leave.getStatus());
            dto.setDateCreated(leave.getDateCreated());

            dto.setApplyToId(leave.getApplyTo().getEmpId());
            dto.setApplyToName(leave.getApplyTo().getFirstName() + " " + leave.getApplyTo().getLastName());

            // calculating leave days
            // long days = ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()) +
            // 1;
            // dto.setQty(days);
            int fromSession = mapSession(leave.getSessionFrom());
            int toSession = mapSession(leave.getSessionTo());
            double quantity = LeaveCalculator.calculateLeaveQuantity(
                    leave.getFromDate(),
                    leave.getToDate(),
                    fromSession,
                    toSession);
            dto.setQty(quantity);

            // set actionBy
            if (leave.getActionBy() != null) {
                dto.setActionById(leave.getActionBy().getEmpId());
                dto.setActionByName(leave.getActionBy().getFirstName() + " " + leave.getActionBy().getLastName());
            }

            dto.setAttachmentPath(leave.getAttachmentPath());

            return dto;
        }).toList();
    }

    // approve leave and deduct balance
    public String approveLeave(int leaveId, int hrEmpId, String approverRole) {
        LeaveApplication leave = leaveRepo.findById(leaveId).orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new RuntimeException("Only pending leaves can be approved by " + approverRole);
        }

        // deduct balance
        if (leave.getLeaveType().getRequiresBalance() != null && leave.getLeaveType().getRequiresBalance()) {
            LeaveBalance balance = leaveBalanceRepo.findByEmployeeEmpIdAndLeaveTypeLeavetypeId(
                    leave.getEmployee().getEmpId(),
                    leave.getLeaveType().getLeavetypeId())
                    .orElseThrow(() -> new RuntimeException("Leave Balance not found"));

            double newBalance = balance.getBalance() - leave.getLeaveQuantity();
            if (newBalance < 0) {
                throw new RuntimeException("Insufficient leave balance at approval");
            }

            balance.setBalance(newBalance);
            leaveBalanceRepo.save(balance);
        }

        // updating status
        leave.setStatus("APPROVED");

        // set actionBy
        Employee hr = employeeRepo.findById(hrEmpId).orElseThrow(() -> new RuntimeException("HR not found"));

        leave.setActionBy(hr);

        leaveRepo.save(leave);

        // sendiong male
        emailService.sendLeaveStatusUpdateEmail(leave);

        return approverRole + " approved leave"
                + (leave.getLeaveType().getRequiresBalance() ? " and balance deducted" : "");

    }

    public String rejectLeave(int leaveId, int hrEmpId, String approverRole) {
        LeaveApplication leave = leaveRepo.findById(leaveId).orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
            throw new RuntimeException("Only pending leaves can be rejected by " + approverRole);
        }

        leave.setStatus("REJECTED");

        // set Action by
        Employee hr = employeeRepo.findById(hrEmpId).orElseThrow(() -> new RuntimeException("HR not found"));
        leave.setActionBy(hr);

        leaveRepo.save(leave);

        // sending mail
        emailService.sendLeaveStatusUpdateEmail(leave);

        return approverRole + " rejected leave";
    }

    // helper method to mapSession
    private int mapSession(String sessionStr) {
        if (sessionStr == null)
            return 1;
        switch (sessionStr.toLowerCase()) {
            case "one":
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            case "four":
                return 4;
            default:
                return 1;

        }
    }

}
