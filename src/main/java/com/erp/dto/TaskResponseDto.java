package com.erp.dto;

import java.time.LocalDateTime;

public class TaskResponseDto {

    private int taskId;
    private String projectName;
    private String subjectName;
    private String typeName;
    private String priorityName;
    private String statusName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int taskQuantity;
    private String taskDescription;

    public TaskResponseDto() {
    }

    public TaskResponseDto(int taskId, String projectName, String subjectName, String typeName, String priorityName,
            String statusName, LocalDateTime startDate, LocalDateTime endDate, int taskQuantity,
            String taskDescription) {
        this.taskId = taskId;
        this.projectName = projectName;
        this.subjectName = subjectName;
        this.typeName = typeName;
        this.priorityName = priorityName;
        this.statusName = statusName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskQuantity = taskQuantity;
        this.taskDescription = taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

}
