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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "system_in_out")
public class SystemInOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_id")
    private int systemInOutId;

    @Column(name = "system_in")
    private LocalDateTime systemIn;

    @Column(name = "system_out")
    private LocalDateTime systemOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @CreationTimestamp
    @Column(name = "date_created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public SystemInOut() {
    }

    public SystemInOut(int systemInOutId, LocalDateTime systemIn, LocalDateTime systemOut, Employee employee,
            LocalDateTime dateCreated, LocalDateTime updatedAt) {
        this.systemInOutId = systemInOutId;
        this.systemIn = systemIn;
        this.systemOut = systemOut;
        this.employee = employee;
        this.dateCreated = dateCreated;
        this.updatedAt = updatedAt;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
