package com.erp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reporting_managers")
public class ReportingManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rm_id")
    private int rm_id;

    private String rm_name1;
    private String rm_name2;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date_created = LocalDateTime.now();

    public ReportingManager() {
    }

    public ReportingManager(int rm_id, String rm_name1, String rm_name2, Department department,
            LocalDateTime date_created) {
        this.rm_id = rm_id;
        this.rm_name1 = rm_name1;
        this.rm_name2 = rm_name2;
        this.department = department;
        this.date_created = date_created;
    }

    public int getRm_id() {
        return rm_id;
    }

    public void setRm_id(int rm_id) {
        this.rm_id = rm_id;
    }

    public String getRm_name1() {
        return rm_name1;
    }

    public void setRm_name1(String rm_name1) {
        this.rm_name1 = rm_name1;
    }

    public String getRm_name2() {
        return rm_name2;
    }

    public void setRm_name2(String rm_name2) {
        this.rm_name2 = rm_name2;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDateTime getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDateTime date_created) {
        this.date_created = date_created;
    }

}
