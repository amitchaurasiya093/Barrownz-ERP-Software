package com.erp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.LeaveApplicationDto;
import com.erp.exception.BalanceNotConfiguredException;
import com.erp.exception.InsufficientBalanceException;
import com.erp.model.Employee;
import com.erp.model.LeaveApplication;
import com.erp.model.LeaveBalance;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LeaveApplicationRepository;
import com.erp.repository.LeaveBalanceRepository;
import com.erp.repository.LeaveTypeRepository;
import com.erp.util.LeaveCalculator;

@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    private final LeaveApplicationRepository leaveRepo;
    private final EmployeeRepository employeeRepo;
    private final LeaveTypeRepository leaveTypeRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    private final EmailService emailService;

    @Value("${file.upload-dir.leaves}")
    private String leaveUploadDir;

    public LeaveApplicationServiceImpl(LeaveApplicationRepository leaveRepo, EmployeeRepository employeeRepo,
            LeaveTypeRepository leaveTypeRepo, LeaveBalanceRepository leaveBalanceRepo, EmailService emailService) {
        this.leaveRepo = leaveRepo;
        this.employeeRepo = employeeRepo;
        this.leaveTypeRepo = leaveTypeRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
        this.emailService = emailService;
    }

    @Override
    public LeaveApplication applyLeave(LeaveApplicationDto dto, String attachmentPath) {

        LeaveApplication leave = new LeaveApplication();
        leave.setEmployee(employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found.")));
        leave.setLeaveType(leaveTypeRepo.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found.")));
        leave.setSessionFrom(dto.getSessionFrom());
        leave.setSessionTo(dto.getSessionTo());
        leave.setFromDate(dto.getFromDate());
        leave.setToDate(dto.getToDate());
        leave.setContactDetails(dto.getContactDetails());
        leave.setApplyTo(employeeRepo.findById(dto.getApplyToId())
                .orElseThrow(() -> new RuntimeException("ApplyTo Employee not found")));

        if (dto.getCcEmployeeIds() != null && !dto.getCcEmployeeIds().isEmpty()) {
            leave.setCcEmployees(employeeRepo.findAllById(dto.getCcEmployeeIds()));
        } else {
            leave.setCcEmployees(new ArrayList<>());
        }
        leave.setReason(dto.getReason());
        leave.setAttachmentPath(attachmentPath);

        leave.setStatus("PENDING");

        // calculate leave quantity
        int fromSession = mapSession(dto.getSessionFrom());
        int toSession = mapSession(dto.getSessionTo());
        double quantity = LeaveCalculator.calculateLeaveQuantity(
                dto.getFromDate(),
                dto.getToDate(),
                fromSession,
                toSession);
        leave.setLeaveQuantity(quantity);

        // 3 ppl rstriction afgter 15 days
        if (Boolean.TRUE.equals(leave.getLeaveType().getRequiresBalance())) {
            LocalDate startOfMonth = leave.getFromDate().withDayOfMonth(1);
            LocalDate endOfMonth = leave.getFromDate().withDayOfMonth(leave.getFromDate().lengthOfMonth());

            List<LeaveApplication> monthlyPaidLeaves = leaveRepo.findByEmployeeAndMonth(
                    leave.getEmployee().getEmpId(),
                    startOfMonth,
                    endOfMonth)
                    .stream().filter(l -> Boolean.TRUE.equals(l.getLeaveType().getRequiresBalance()))
                    .toList();

            double totalPaidLeavesTaken = monthlyPaidLeaves.stream()
                    .mapToDouble(LeaveApplication::getLeaveQuantity)
                    .sum();

            if (leave.getFromDate().getDayOfMonth() > 15 && (totalPaidLeavesTaken + quantity) > 3) {
                throw new IllegalArgumentException(
                        "You are trying to take 3 paid leaves after 15th of the month. Please chose LOP instead.");
            }
        }

        // balance check for paid leave
        if (leave.getLeaveType().getRequiresBalance() != null && leave.getLeaveType().getRequiresBalance()) {
            LeaveBalance balance = leaveBalanceRepo.findByEmployeeEmpIdAndLeaveTypeLeavetypeId(
                    dto.getEmployeeId(), dto.getLeaveTypeId())
                    .orElseThrow(
                            () -> new InsufficientBalanceException("No Leave Balance configured for this employee"));

            if (balance.getBalance() <= 0) {
                throw new InsufficientBalanceException(
                        "You do not have sufficient leave balance. Please choose a leave type that do not require balance.");
            }

            if (balance.getBalance() < quantity) {
                throw new InsufficientBalanceException("Insufficient leave balance for requested days.");
            }
        }

        // LeaveBalance balance = leaveBalanceRepo
        // .findByEmployeeEmpIdAndLeaveTypeLeavetypeId(dto.getEmployeeId(),
        // dto.getLeaveTypeId())
        // .orElseThrow(() -> new RuntimeException("No leave balance configured"));

        // // checking if employee jas extra balance
        // if (balance.getLeaveType().getLeaveType().equalsIgnoreCase("Paid Leave")) {
        // if (balance.getBalance() < quantity) {
        // throw new RuntimeException("Insufficient leave balance");
        // }
        // }

        leave.setStatus("PENDING");

        LeaveApplication savedLeave = leaveRepo.save(leave);
        emailService.sendLeaveAppliedEmail(savedLeave);
        return savedLeave;
    }

    // @Override
    // public List<LeaveApplication> getLeavesByEmployee(int employeeId) {
    // return leaveRepo.findByEmployeeEmpId(employeeId);
    // }

    @Override
    public List<LeaveApplicationDto> getLeavesByEmployee(int employeeId) {
        List<LeaveApplication> leaves = leaveRepo.findByEmployeeEmpId(employeeId);

        return leaves.stream().map(leave -> {
            LeaveApplicationDto dto = new LeaveApplicationDto();

            dto.setLeaveApplicationId(leave.getLeaveApplicationId());

            // employee details
            dto.setEmployeeId(leave.getEmployee().getEmpId());
            dto.setEmployeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName());
            dto.setEmployeeCode(leave.getEmployee().getEmpCode());

            // leave type
            dto.setLeaveTypeId(leave.getLeaveType().getLeavetypeId());
            dto.setLeaveTypeName(leave.getLeaveType().getLeaveType());

            // dates
            dto.setSessionFrom(leave.getSessionFrom());
            dto.setSessionTo(leave.getSessionTo());
            dto.setFromDate(leave.getFromDate());
            dto.setToDate(leave.getToDate());
            dto.setAppliedDate(leave.getDateCreated());

            // qty
            // int fromSession = mapSession(leave.getSessionFrom());
            // int toSession = mapSession(leave.getSessionTo());

            // double quantity = LeaveCalculator.calculateLeaveQuantity(leave.getFromDate(),
            // leave.getToDate(),
            // fromSession, toSession);

            // dto.setLeaveQuantity(quantity);

            dto.setLeaveQuantity(leave.getLeaveQuantity());

            dto.setContactDetails(leave.getContactDetails());

            // apply to
            dto.setApplyToId(leave.getApplyTo().getEmpId());
            dto.setApplyToName(leave.getApplyTo().getFirstName() + " " + leave.getApplyTo().getLastName());

            dto.setReason(leave.getReason());

            // cc employees
            dto.setCcEmployeeIds(
                    leave.getCcEmployees().stream().map(e -> e.getEmpId()).toList());

            dto.setCcEmployeeNames(
                    leave.getCcEmployees().stream()
                            .map(e -> e.getFirstName() + " " + e.getLastName())
                            .toList());

            // getActionBy
            if (leave.getActionBy() != null) {
                dto.setActionById(leave.getActionBy().getEmpId());
                dto.setActionByName(
                        leave.getActionBy().getFirstName() + " " + leave.getActionBy().getLastName());
            }

            dto.setStatus(leave.getStatus());

            return dto;
        }).toList();

    }

    // helper method to handle file uploads

    public String saveAttachment(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;

        String contentType = file.getContentType();
        if (!(contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("application/pdf"))) {
            throw new IllegalArgumentException("Only Jpg, jpeg, png or pdf files are allowed!");
        }
        File dir = new File(leaveUploadDir);
        if (!dir.exists())
            dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(leaveUploadDir, fileName);

        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return leaveUploadDir + fileName;
    }

    @Override
    public boolean updateLeaveStatusByRM(int leaveId, String status, int rmEmpId) {
        LeaveApplication leave = leaveRepo.findById(leaveId).orElse(null);
        if (leave == null)
            return false;

        // ensuring the loggedin employee is apply to of the leave
        if (leave.getApplyTo() == null || leave.getApplyTo().getEmpId() != rmEmpId) {
            return false;
        }

        // deduct balance
        if ("APPROVED".equalsIgnoreCase(status)
                && leave.getLeaveType().getRequiresBalance() != null
                && leave.getLeaveType().getRequiresBalance()) {

            LeaveBalance balance = leaveBalanceRepo
                    .findByEmployeeEmpIdAndLeaveTypeLeavetypeId(
                            leave.getEmployee().getEmpId(), leave.getLeaveType().getLeavetypeId())
                    .orElseThrow(() -> new BalanceNotConfiguredException("Leave balance not configured"));

            double newBalance = balance.getBalance() - leave.getLeaveQuantity();
            if (newBalance < 0) {
                throw new InsufficientBalanceException("Insufficient balance at approval");
            }

            balance.setBalance(newBalance);
            leaveBalanceRepo.save(balance);
        }

        leave.setStatus(status.toUpperCase());

        // code for setting action(approved/reject by) taken by..
        Employee rm = employeeRepo.findById(rmEmpId).orElseThrow(() -> new RuntimeException("RM not found"));

        leave.setActionBy(rm);

        leaveRepo.save(leave);

        // sending mail
        emailService.sendLeaveStatusUpdateEmail(leave);

        return true;
    }

    // RM DTO mapper
    @Override
    public List<LeaveApplicationDto> getLeavesForRM(int rmEmpId) {
        List<LeaveApplication> leaves = leaveRepo.findByApplyToEmpId(rmEmpId);

        return leaves.stream().map(leave -> {
            LeaveApplicationDto dto = new LeaveApplicationDto();

            dto.setLeaveApplicationId(leave.getLeaveApplicationId());

            // employee details
            dto.setEmployeeId(leave.getEmployee().getEmpId());
            dto.setEmployeeName(leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName());
            dto.setEmployeeCode(leave.getEmployee().getEmpCode());

            // leave type
            dto.setLeaveTypeId(leave.getLeaveType().getLeavetypeId());
            dto.setLeaveTypeName(leave.getLeaveType().getLeaveType());

            // dates
            dto.setSessionFrom(leave.getSessionFrom());
            dto.setSessionTo(leave.getSessionTo());
            dto.setFromDate(leave.getFromDate());
            dto.setToDate(leave.getToDate());
            dto.setAppliedDate(leave.getDateCreated());

            // qty
            // int fromSession = mapSession(leave.getSessionFrom());
            // int toSession = mapSession(leave.getSessionTo());

            // double quantity = LeaveCalculator.calculateLeaveQuantity(leave.getFromDate(),
            // leave.getToDate(),
            // fromSession, toSession);

            // dto.setLeaveQuantity(quantity);

            dto.setLeaveQuantity(leave.getLeaveQuantity());

            dto.setContactDetails(leave.getContactDetails());

            // apply to
            dto.setApplyToId(leave.getApplyTo().getEmpId());
            dto.setApplyToName(leave.getApplyTo().getFirstName() + " " + leave.getApplyTo().getLastName());

            dto.setReason(leave.getReason());

            // cc employees
            // dto.setCcEmployeeIds(
            // leave.getCcEmployees().stream().map(e -> e.getEmpId()).toList());
            dto.setCcEmployeeIds(
                    leave.getCcEmployees().stream()
                            .map(Employee::getEmpId)
                            .toList());

            dto.setCcEmployeeNames(
                    leave.getCcEmployees().stream()
                            .map(e -> e.getFirstName() + " " + e.getLastName())
                            .toList());

            // get action By
            if (leave.getActionBy() != null) {
                dto.setActionById(leave.getActionBy().getEmpId());
                dto.setActionByName(leave.getActionBy().getFirstName() + " " + leave.getActionBy().getLastName());
            }

            dto.setStatus(leave.getStatus());

            return dto;
        }).toList();
    }

    // helper method for converting string session from , session To
    private int mapSession(String session) {
        if (session == null)
            return 1;
        return switch (session.trim().toLowerCase()) {
            case "session 1", "1", "one" -> 1;
            case "session 2", "2", "two" -> 2;
            case "session 3", "3", "three" -> 3;
            case "session 4", "4", "four" -> 4;
            default -> 1;
        };
    }

    @Override
    public double getMonthlyLeaveTotal(int employeeId, int month, int year) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate startOfMonth = ym.atDay(1);
        LocalDate endOfMonth = ym.atEndOfMonth();

        return leaveRepo.findByEmployeeAndMonth(employeeId, startOfMonth, endOfMonth)
                .stream()
                .mapToDouble(LeaveApplication::getLeaveQuantity)
                .sum();
    }

}
