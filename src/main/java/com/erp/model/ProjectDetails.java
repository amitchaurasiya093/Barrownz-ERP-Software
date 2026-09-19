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
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "project_details")
public class ProjectDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_details_id")
    private int projectDetailsid;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private TaskProject projectName;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TaskType projectType;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private TaskPriority projectPriority;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus project_status;

    // project dates

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate submissionDate;

    @ManyToOne
    @JoinColumn(name = "project_leader_id")
    private Employee projectLeader;

    private Double Rate;

    @Column(name = "rate_type", length = 10)
    private String rateType;

    @ManyToMany
    @JoinTable(name = "project_team_members", joinColumns = @JoinColumn(name = "project_details_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> teamMembers;

    @Column(name = "man_per_hour")
    private Double manPerHour;

    @Column(name = "rate_calculation")
    private Double rateCalculation;

    public String remarks;

    @Column(columnDefinition = "TEXT")
    public String description;

    @CreationTimestamp
    @Column(name = "date_created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public LocalDateTime dateCreated = LocalDateTime.now();

    public ProjectDetails() {
    }

    public ProjectDetails(int projectDetailsid, TaskProject projectName, Client client, TaskType projectType,
            TaskPriority projectPriority, TaskStatus project_status, LocalDate startDate, LocalDate endDate,
            LocalDate submissionDate, Employee projectLeader, Double rate, String rateType, List<Employee> teamMembers,
            Double manPerHour, Double rateCalculation, String remarks, String description, LocalDateTime dateCreated) {
        this.projectDetailsid = projectDetailsid;
        this.projectName = projectName;
        this.client = client;
        this.projectType = projectType;
        this.projectPriority = projectPriority;
        this.project_status = project_status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submissionDate = submissionDate;
        this.projectLeader = projectLeader;
        Rate = rate;
        this.rateType = rateType;
        this.teamMembers = teamMembers;
        this.manPerHour = manPerHour;
        this.rateCalculation = rateCalculation;
        this.remarks = remarks;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public int getProjectDetailsid() {
        return projectDetailsid;
    }

    public void setProjectDetailsid(int projectDetailsid) {
        this.projectDetailsid = projectDetailsid;
    }

    public TaskProject getProjectName() {
        return projectName;
    }

    public void setProjectName(TaskProject projectName) {
        this.projectName = projectName;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public TaskType getProjectType() {
        return projectType;
    }

    public void setProjectType(TaskType projectType) {
        this.projectType = projectType;
    }

    public TaskPriority getProjectPriority() {
        return projectPriority;
    }

    public void setProjectPriority(TaskPriority projectPriority) {
        this.projectPriority = projectPriority;
    }

    public TaskStatus getProject_status() {
        return project_status;
    }

    public void setProject_status(TaskStatus project_status) {
        this.project_status = project_status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Employee getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(Employee projectLeader) {
        this.projectLeader = projectLeader;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public List<Employee> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<Employee> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Double getManPerHour() {
        return manPerHour;
    }

    public void setManPerHour(Double manPerHour) {
        this.manPerHour = manPerHour;
    }

    public Double getRateCalculation() {
        return rateCalculation;
    }

    public void setRateCalculation(Double rateCalculation) {
        this.rateCalculation = rateCalculation;
    }

}
