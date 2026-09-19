package com.erp.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "leavetypes")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leavetype_id")
    private int leavetypeId;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "allowed_for_probation")
    private Boolean allowedForProbation = false;

    @Column(name = "requires_balance", nullable = false)
    private Boolean requiresBalance = false;

    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated = LocalDateTime.now();

    public LeaveType() {
    }

    public LeaveType(int leavetypeId, String leaveType, Boolean allowedForProbation, Boolean requiresBalance,
            LocalDateTime dateCreated) {
        this.leavetypeId = leavetypeId;
        this.leaveType = leaveType;
        this.allowedForProbation = allowedForProbation;
        this.requiresBalance = requiresBalance;
        this.dateCreated = dateCreated;
    }

    public int getLeavetypeId() {
        return leavetypeId;
    }

    public void setLeavetypeId(int leavetypeId) {
        this.leavetypeId = leavetypeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Boolean getAllowedForProbation() {
        return allowedForProbation;
    }

    public void setAllowedForProbation(Boolean allowedForProbation) {
        this.allowedForProbation = allowedForProbation;
    }

    public Boolean getRequiresBalance() {
        return requiresBalance;
    }

    public void setRequiresBalance(Boolean requiresBalance) {
        this.requiresBalance = requiresBalance;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

}
