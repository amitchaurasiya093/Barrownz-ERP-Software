package com.erp.dto;

public class LoginRequest {

    private int empCode;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(int empCode, String password) {
        this.empCode = empCode;
        this.password = password;
    }

    public int getEmpCode() {
        return empCode;
    }

    public void setEmpCode(int empCode) {
        this.empCode = empCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
