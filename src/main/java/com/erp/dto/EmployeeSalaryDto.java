package com.erp.dto;

public class EmployeeSalaryDto {

    private int salaryId;
    private int empId;
    private Double monthlySalary;
    private String employeeName;

    public EmployeeSalaryDto() {
    }

    public EmployeeSalaryDto(int salaryId, int empId, Double monthlySalary, String employeeName) {
        this.salaryId = salaryId;
        this.empId = empId;
        this.monthlySalary = monthlySalary;
        this.employeeName = employeeName;
    }

    public int getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public Double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(Double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

}
