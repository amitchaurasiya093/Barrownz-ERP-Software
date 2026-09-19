package com.erp.dto;

public class EmployeeEducationDetailsDto {

    private int educationDetailsId;
    private String qualification;
    private String university;
    private String roleNumber;
    private String subject;
    private String passingYear;
    private String marks;
    private Integer employeeId;

    public EmployeeEducationDetailsDto() {
    }

    public EmployeeEducationDetailsDto(int educationDetailsId, String qualification, String university,
            String roleNumber, String subject, String passingYear, String marks, Integer employeeId) {
        this.educationDetailsId = educationDetailsId;
        this.qualification = qualification;
        this.university = university;
        this.roleNumber = roleNumber;
        this.subject = subject;
        this.passingYear = passingYear;
        this.marks = marks;
        this.employeeId = employeeId;
    }

    public int getEducationDetailsId() {
        return educationDetailsId;
    }

    public void setEducationDetailsId(int educationDetailsId) {
        this.educationDetailsId = educationDetailsId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getRoleNumber() {
        return roleNumber;
    }

    public void setRoleNumber(String roleNumber) {
        this.roleNumber = roleNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(String passingYear) {
        this.passingYear = passingYear;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

}
