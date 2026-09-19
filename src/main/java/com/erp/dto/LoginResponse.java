package com.erp.dto;

public class LoginResponse {

    private String token;
    private String role;
    private int empCode;
    private int empId;

    public LoginResponse() {
    }

    public LoginResponse(String token, String role, int empCode, int empId) {
        this.token = token;
        this.role = role;
        this.empCode = empCode;
        this.empId = empId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getEmpCode() {
        return empCode;
    }

    public void setEmpCode(int empCode) {
        this.empCode = empCode;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

}
