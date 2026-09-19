package com.erp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeDto {

    private int empId;
    private int emp_code;
    private String first_name;
    private String last_name;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String personal_email;
    private String address;
    private String contact;
    private LocalDate joining_date;
    private String company;
    private String employee_status;
    private String joining_status;
    private String working_status;

    private int departmentId;
    private String departmentName;

    private int levelId;
    private BigDecimal levelName;

    private int shiftId;
    private String shiftName;

    private int roleId;
    private String roleName;

    private Integer reportingManager1Id;
    private String reportingManager1Name;

    private Integer reportingManager1EmployeeId;

    private Integer reportingManager2Id;
    private String reportingManager2Name;

    private String password;
    private String profile_picture;

    public EmployeeDto() {
    }

    public EmployeeDto(int empId, int emp_code, String first_name, String last_name, LocalDate dateOfBirth,
            String gender, String email, String personal_email, String address, String contact, LocalDate joining_date,
            String company, String employee_status, String joining_status, String working_status, int departmentId,
            String departmentName, int levelId, BigDecimal levelName, int shiftId, String shiftName, int roleId,
            String roleName, Integer reportingManager1Id, String reportingManager1Name,
            Integer reportingManager1EmployeeId, Integer reportingManager2Id, String reportingManager2Name,
            String password, String profile_picture) {
        this.empId = empId;
        this.emp_code = emp_code;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.personal_email = personal_email;
        this.address = address;
        this.contact = contact;
        this.joining_date = joining_date;
        this.company = company;
        this.employee_status = employee_status;
        this.joining_status = joining_status;
        this.working_status = working_status;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.levelId = levelId;
        this.levelName = levelName;
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.reportingManager1Id = reportingManager1Id;
        this.reportingManager1Name = reportingManager1Name;
        this.reportingManager1EmployeeId = reportingManager1EmployeeId;
        this.reportingManager2Id = reportingManager2Id;
        this.reportingManager2Name = reportingManager2Name;
        this.password = password;
        this.profile_picture = profile_picture;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(int emp_code) {
        this.emp_code = emp_code;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonal_email() {
        return personal_email;
    }

    public void setPersonal_email(String personal_email) {
        this.personal_email = personal_email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getJoining_date() {
        return joining_date;
    }

    public void setJoining_date(LocalDate joining_date) {
        this.joining_date = joining_date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmployee_status() {
        return employee_status;
    }

    public void setEmployee_status(String employee_status) {
        this.employee_status = employee_status;
    }

    public String getJoining_status() {
        return joining_status;
    }

    public void setJoining_status(String joining_status) {
        this.joining_status = joining_status;
    }

    public String getWorking_status() {
        return working_status;
    }

    public void setWorking_status(String working_status) {
        this.working_status = working_status;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public BigDecimal getLevelName() {
        return levelName;
    }

    public void setLevelName(BigDecimal levelName) {
        this.levelName = levelName;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getReportingManager1Id() {
        return reportingManager1Id;
    }

    public void setReportingManager1Id(Integer reportingManager1Id) {
        this.reportingManager1Id = reportingManager1Id;
    }

    public String getReportingManager1Name() {
        return reportingManager1Name;
    }

    public void setReportingManager1Name(String reportingManager1Name) {
        this.reportingManager1Name = reportingManager1Name;
    }

    public Integer getReportingManager2Id() {
        return reportingManager2Id;
    }

    public void setReportingManager2Id(Integer reportingManager2Id) {
        this.reportingManager2Id = reportingManager2Id;
    }

    public String getReportingManager2Name() {
        return reportingManager2Name;
    }

    public void setReportingManager2Name(String reportingManager2Name) {
        this.reportingManager2Name = reportingManager2Name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Integer getReportingManager1EmployeeId() {
        return reportingManager1EmployeeId;
    }

    public void setReportingManager1EmployeeId(Integer reportingManager1EmployeeId) {
        this.reportingManager1EmployeeId = reportingManager1EmployeeId;
    }

}
