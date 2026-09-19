package com.erp.service;

import java.util.List;

import com.erp.model.TaskStatus;

public interface TaskStatusService {

    public TaskStatus saveTaskStatus(TaskStatus taskStatus);

    public List<TaskStatus> getAllTaskStatus();

    public TaskStatus getSingleTaskStatus(int id);

    public TaskStatus updateTaskStatus(int id, TaskStatus taskStatus);

    public void deleteTaskStatus(int id);

}
