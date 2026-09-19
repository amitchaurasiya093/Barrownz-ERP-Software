package com.erp.dto;

import java.time.LocalDateTime;

public class SystemInOutDto {

    private int systemInOutId;
    private LocalDateTime systemIn;
    private LocalDateTime systemOut;
    private int employeeId;
    private String employeeName;

    public SystemInOutDto() {
    }

    public SystemInOutDto(int systemInOutId, LocalDateTime systemIn, LocalDateTime systemOut, int employeeId,
            String employeeName) {
        this.systemInOutId = systemInOutId;
        this.systemIn = systemIn;
        this.systemOut = systemOut;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public int getSystemInOutId() {
        return systemInOutId;
    }

    public void setSystemInOutId(int systemInOutId) {
        this.systemInOutId = systemInOutId;
    }

    public LocalDateTime getSystemIn() {
        return systemIn;
    }

    public void setSystemIn(LocalDateTime systemIn) {
        this.systemIn = systemIn;
    }

    public LocalDateTime getSystemOut() {
        return systemOut;
    }

    public void setSystemOut(LocalDateTime systemOut) {
        this.systemOut = systemOut;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

}
