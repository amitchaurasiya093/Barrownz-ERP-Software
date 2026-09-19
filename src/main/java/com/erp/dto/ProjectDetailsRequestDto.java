package com.erp.dto;

import java.time.LocalDate;
import java.util.List;

public class ProjectDetailsRequestDto {

    private int ProjectDetailsId;

    private int projectId;
    private String projectName;

    private int clientId;
    private String clientName;

    private int typeId;
    private String typeName;

    private int priorityId;
    private String priorityName;

    private int statusId;
    private String statusName;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate submissionDate;

    private int projectLeaderId;
    private String projectLeaderName;

    private Double rate;
    private String rateType;

    private List<Integer> teamMemberIds;
    private List<String> teamMemberName;

    private double manPerHour;
    private double rateCalculation;

    private String remarks;
    private String description;

    public ProjectDetailsRequestDto() {
    }

    public ProjectDetailsRequestDto(int projectDetailsId, int projectId, String projectName, int clientId,
            String clientName, int typeId, String typeName, int priorityId, String priorityName, int statusId,
            String statusName, LocalDate startDate, LocalDate endDate, LocalDate submissionDate, int projectLeaderId,
            String projectLeaderName, Double rate, String rateType, List<Integer> teamMemberIds,
            List<String> teamMemberName, double manPerHour, double rateCalculation, String remarks,
            String description) {
        ProjectDetailsId = projectDetailsId;
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientId = clientId;
        this.clientName = clientName;
        this.typeId = typeId;
        this.typeName = typeName;
        this.priorityId = priorityId;
        this.priorityName = priorityName;
        this.statusId = statusId;
        this.statusName = statusName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.submissionDate = submissionDate;
        this.projectLeaderId = projectLeaderId;
        this.projectLeaderName = projectLeaderName;
        this.rate = rate;
        this.rateType = rateType;
        this.teamMemberIds = teamMemberIds;
        this.teamMemberName = teamMemberName;
        this.manPerHour = manPerHour;
        this.rateCalculation = rateCalculation;
        this.remarks = remarks;
        this.description = description;
    }

    public int getProjectDetailsId() {
        return ProjectDetailsId;
    }

    public void setProjectDetailsId(int projectDetailsId) {
        ProjectDetailsId = projectDetailsId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public int getProjectLeaderId() {
        return projectLeaderId;
    }

    public void setProjectLeaderId(int projectLeaderId) {
        this.projectLeaderId = projectLeaderId;
    }

    public String getProjectLeaderName() {
        return projectLeaderName;
    }

    public void setProjectLeaderName(String projectLeaderName) {
        this.projectLeaderName = projectLeaderName;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public List<Integer> getTeamMemberIds() {
        return teamMemberIds;
    }

    public void setTeamMemberIds(List<Integer> teamMemberIds) {
        this.teamMemberIds = teamMemberIds;
    }

    public List<String> getTeamMemberName() {
        return teamMemberName;
    }

    public void setTeamMemberName(List<String> teamMemberName) {
        this.teamMemberName = teamMemberName;
    }

    public double getManPerHour() {
        return manPerHour;
    }

    public void setManPerHour(double manPerHour) {
        this.manPerHour = manPerHour;
    }

    public double getRateCalculation() {
        return rateCalculation;
    }

    public void setRateCalculation(double rateCalculation) {
        this.rateCalculation = rateCalculation;
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

}
