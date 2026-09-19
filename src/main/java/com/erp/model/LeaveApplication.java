package com.erp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "leave_applications")
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_application_id")
    private int leaveApplicationId;

    private String sessionFrom;
    private String sessionTo;

    private LocalDate fromDate;
    private LocalDate toDate;

    private String contactDetails;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @ManyToOne
    @JoinColumn(name = "apply_to_manager_id")
    private Employee applyTo;

    @ManyToMany
    @JoinTable(name = "leave_cc_employees", joinColumns = @JoinColumn(name = "leave_application_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> ccEmployees;

    private String attachmentPath;

    @Lob
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "leave_quantity")
    private double leaveQuantity;

    @ManyToOne
    @JoinColumn(name = "action_by")
    private Employee actionBy;

    private String status = "PENDING";

    @CreationTimestamp
    @Column(name = "date_created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated = LocalDateTime.now();

    public LeaveApplication() {
    }

    public LeaveApplication(int leaveApplicationId, String sessionFrom, String sessionTo, LocalDate fromDate,
            LocalDate toDate, String contactDetails, Employee employee, LeaveType leaveType, Employee applyTo,
            List<Employee> ccEmployees, String attachmentPath, String reason, double leaveQuantity, Employee actionBy,
            String status, LocalDateTime dateCreated) {
        this.leaveApplicationId = leaveApplicationId;
        this.sessionFrom = sessionFrom;
        this.sessionTo = sessionTo;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.contactDetails = contactDetails;
        this.employee = employee;
        this.leaveType = leaveType;
        this.applyTo = applyTo;
        this.ccEmployees = ccEmployees;
        this.attachmentPath = attachmentPath;
        this.reason = reason;
        this.leaveQuantity = leaveQuantity;
        this.actionBy = actionBy;
        this.status = status;
        this.dateCreated = dateCreated;
    }

    public int getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(int leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    public String getSessionFrom() {
        return sessionFrom;
    }

    public void setSessionFrom(String sessionFrom) {
        this.sessionFrom = sessionFrom;
    }

    public String getSessionTo() {
        return sessionTo;
    }

    public void setSessionTo(String sessionTo) {
        this.sessionTo = sessionTo;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Employee getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(Employee applyTo) {
        this.applyTo = applyTo;
    }

    public List<Employee> getCcEmployees() {
        return ccEmployees;
    }

    public void setCcEmployees(List<Employee> ccEmployees) {
        this.ccEmployees = ccEmployees;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public double getLeaveQuantity() {
        return leaveQuantity;
    }

    public void setLeaveQuantity(double leaveQuantity) {
        this.leaveQuantity = leaveQuantity;
    }

    public Employee getActionBy() {
        return actionBy;
    }

    public void setActionBy(Employee actionBy) {
        this.actionBy = actionBy;
    }

}
