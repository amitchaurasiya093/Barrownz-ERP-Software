package com.erp.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_education_details")
public class EmployeeEducationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_details_id")
    private int EducationDetailsId;

    private String qualification;
    private String university;

    @Column(name = "role_number")
    private String roleNumber;
    private String subject;

    @Column(name = "passing_year")
    private String passingYear;
    private String marks;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @CreationTimestamp
    @Column(name = "date_created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateAssigned = LocalDateTime.now();

    public EmployeeEducationDetails() {
    }

    public EmployeeEducationDetails(int educationDetailsId, String qualification, String university, String roleNumber,
            String subject, String passingYear, String marks, Employee employee, LocalDateTime dateAssigned) {
        EducationDetailsId = educationDetailsId;
        this.qualification = qualification;
        this.university = university;
        this.roleNumber = roleNumber;
        this.subject = subject;
        this.passingYear = passingYear;
        this.marks = marks;
        this.employee = employee;
        this.dateAssigned = dateAssigned;
    }

    public int getEducationDetailsId() {
        return EducationDetailsId;
    }

    public void setEducationDetailsId(int educationDetailsId) {
        EducationDetailsId = educationDetailsId;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(LocalDateTime dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

}
