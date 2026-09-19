package com.erp.dto;

import java.time.LocalDateTime;

public class TaskRequestDto {

    private int typeId;
    private int priorityId;
    private int statusId;
    private int subjectId;
    private int projectId;
    private int employeeId;
    private int taskQuantity;
    private String taskDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TaskRequestDto() {
    }

    public TaskRequestDto(int typeId, int priorityId, int statusId, int subjectId, int projectId, int employeeId,
            int taskQuantity, String taskDescription, LocalDateTime startDate, LocalDateTime endDate) {
        this.typeId = typeId;
        this.priorityId = priorityId;
        this.statusId = statusId;
        this.subjectId = subjectId;
        this.projectId = projectId;
        this.employeeId = employeeId;
        this.taskQuantity = taskQuantity;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getTaskQuantity() {
        return taskQuantity;
    }

    public void setTaskQuantity(int taskQuantity) {
        this.taskQuantity = taskQuantity;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

}
