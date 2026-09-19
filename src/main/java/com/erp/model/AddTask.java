package com.erp.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class AddTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private TaskProject taskProject;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus taskStatus;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private TaskPriority taskPriority;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private TaskSubject taskSubject;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "task_quantity")
    private int taskQuantity;

    @Lob
    private String taskDescription;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @CreationTimestamp
    @Column(name = "date_created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public AddTask() {
    }

    public AddTask(int taskId, TaskProject taskProject, TaskType taskType, TaskStatus taskStatus,
            TaskPriority taskPriority, TaskSubject taskSubject, Employee employee, int taskQuantity,
            String taskDescription, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime dateCreated,
            LocalDateTime updatedAt) {
        this.taskId = taskId;
        this.taskProject = taskProject;
        this.taskType = taskType;
        this.taskStatus = taskStatus;
        this.taskPriority = taskPriority;
        this.taskSubject = taskSubject;
        this.employee = employee;
        this.taskQuantity = taskQuantity;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dateCreated = dateCreated;
        this.updatedAt = updatedAt;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TaskProject getTaskProject() {
        return taskProject;
    }

    public void setTaskProject(TaskProject taskProject) {
        this.taskProject = taskProject;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public TaskSubject getTaskSubject() {
        return taskSubject;
    }

    public void setTaskSubject(TaskSubject taskSubject) {
        this.taskSubject = taskSubject;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
