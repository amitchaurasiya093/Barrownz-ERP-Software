package com.erp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LeaveApplicationDto {

    private int LeaveApplicationId;

    private int employeeId;
    private String employeeName;
    private int employeeCode;

    private int leaveTypeId;
    private String leaveTypeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String sessionFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String sessionTo;

    private LocalDate fromDate;
    private LocalDate toDate;
    private String contactDetails;

    private int applyToId;
    private String applyToName;

    private List<Integer> ccEmployeeIds;
    private List<String> ccEmployeeNames;

    private String reason;

    private String status;

    private LocalDateTime appliedDate;
    private Double leaveQuantity;

    private Integer actionById;
    private String actionByName;

    public LeaveApplicationDto() {
    }

    public LeaveApplicationDto(int leaveApplicationId, int employeeId, String employeeName, int employeeCode,
            int leaveTypeId, String leaveTypeName, String sessionFrom, String sessionTo, LocalDate fromDate,
            LocalDate toDate, String contactDetails, int applyToId, String applyToName, List<Integer> ccEmployeeIds,
            List<String> ccEmployeeNames, String reason, String status, LocalDateTime appliedDate, Double leaveQuantity,
            Integer actionById, String actionByName) {
        LeaveApplicationId = leaveApplicationId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeCode = employeeCode;
        this.leaveTypeId = leaveTypeId;
        this.leaveTypeName = leaveTypeName;
        this.sessionFrom = sessionFrom;
        this.sessionTo = sessionTo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.contactDetails = contactDetails;
        this.applyToId = applyToId;
        this.applyToName = applyToName;
        this.ccEmployeeIds = ccEmployeeIds;
        this.ccEmployeeNames = ccEmployeeNames;
        this.reason = reason;
        this.status = status;
        this.appliedDate = appliedDate;
        this.leaveQuantity = leaveQuantity;
        this.actionById = actionById;
        this.actionByName = actionByName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        this.sessionFrom = sessionFrom;
    }

    public String getSessionTo() {
        return sessionTo;
    }

    public void setSessionTo(String sessionTo) {
        this.sessionTo = sessionTo;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public int getApplyToId() {
        return applyToId;
    }

    public void setApplyToId(int applyToId) {
        this.applyToId = applyToId;
    }

    public List<Integer> getCcEmployeeIds() {
        return ccEmployeeIds;
    }

    public void setCcEmployeeIds(List<Integer> ccEmployeeIds) {
        this.ccEmployeeIds = ccEmployeeIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLeaveTypeName() {
        return leaveTypeName;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(int employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLeaveApplicationId() {
        return LeaveApplicationId;
    }

    public void setLeaveApplicationId(int leaveApplicationId) {
        LeaveApplicationId = leaveApplicationId;
    }

    public String getApplyToName() {
        return applyToName;
    }

    public void setApplyToName(String applyToName) {
        this.applyToName = applyToName;
    }

    public List<String> getCcEmployeeNames() {
        return ccEmployeeNames;
    }

    public void setCcEmployeeNames(List<String> ccEmployeeNames) {
        this.ccEmployeeNames = ccEmployeeNames;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public Double getLeaveQuantity() {
        return leaveQuantity;
    }

    public void setLeaveQuantity(Double leaveQuantity) {
        this.leaveQuantity = leaveQuantity;
    }

    public Integer getActionById() {
        return actionById;
    }

    public void setActionById(Integer actionById) {
        this.actionById = actionById;
    }

    public String getActionByName() {
        return actionByName;
    }

    public void setActionByName(String actionByName) {
        this.actionByName = actionByName;
    }

}
