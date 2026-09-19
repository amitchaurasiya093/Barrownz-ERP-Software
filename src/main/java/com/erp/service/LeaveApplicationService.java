package com.erp.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.LeaveApplicationDto;
import com.erp.model.LeaveApplication;

public interface LeaveApplicationService {

    public LeaveApplication applyLeave(LeaveApplicationDto dto, String attachmentPath);

    public List<LeaveApplicationDto> getLeavesByEmployee(int employeeId);

    public String saveAttachment(MultipartFile file);

    public boolean updateLeaveStatusByRM(int leaveId, String status, int rmEmpId);

    public List<LeaveApplicationDto> getLeavesForRM(int rmEmpId);

    // get total monthly leave
    public double getMonthlyLeaveTotal(int employeeId, int month, int year);

}
