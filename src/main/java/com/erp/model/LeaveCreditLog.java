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
@Table(name = "leave_credit_log")
public class LeaveCreditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_credit_log_id")
    private int leaveCreditLogId;

    @Column(name = "credit_month")
    private int creditMonth;

    @Column(name = "credit_year")
    private int creditYear;

    @CreationTimestamp
    @Column(name = "credited_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creditedAt = LocalDateTime.now();

    public LeaveCreditLog() {
    }

    public LeaveCreditLog(int leaveCreditLogId, int creditMonth, int creditYear, LocalDateTime creditedAt) {
        this.leaveCreditLogId = leaveCreditLogId;
        this.creditMonth = creditMonth;
        this.creditYear = creditYear;
        this.creditedAt = creditedAt;
    }

    public int getLeaveCreditLogId() {
        return leaveCreditLogId;
    }

    public void setLeaveCreditLogId(int leaveCreditLogId) {
        this.leaveCreditLogId = leaveCreditLogId;
    }

    public int getCreditMonth() {
        return creditMonth;
    }

    public void setCreditMonth(int creditMonth) {
        this.creditMonth = creditMonth;
    }

    public int getCreditYear() {
        return creditYear;
    }

    public void setCreditYear(int creditYear) {
        this.creditYear = creditYear;
    }

    public LocalDateTime getCreditedAt() {
        return creditedAt;
    }

    public void setCreditedAt(LocalDateTime creditedAt) {
        this.creditedAt = creditedAt;
    }

}
