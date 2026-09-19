package com.erp.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "assign_employee_project")
public class AssignEmployeeProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_project_id")
    private int empProjectId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private Employee employee;

    @Column(name = "emp_project_name")
    private String empProjectName;

    @CreationTimestamp
    @Column(name = "date_created", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated = LocalDateTime.now();

    public AssignEmployeeProject() {
    }

    public AssignEmployeeProject(int empProjectId, Employee employee, String empProjectName,
            LocalDateTime dateCreated) {
        this.empProjectId = empProjectId;
        this.employee = employee;
        this.empProjectName = empProjectName;
        this.dateCreated = dateCreated;
    }

    public int getEmpProjectId() {
        return empProjectId;
    }

    public void setEmpProjectId(int empProjectId) {
        this.empProjectId = empProjectId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getEmpProjectName() {
        return empProjectName;
    }

    public void setEmpProjectName(String empProjectName) {
        this.empProjectName = empProjectName;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

}
