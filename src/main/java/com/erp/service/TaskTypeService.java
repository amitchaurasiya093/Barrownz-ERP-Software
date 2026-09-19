package com.erp.service;

import java.util.List;

import com.erp.model.TaskType;

public interface TaskTypeService {

    public TaskType saveTaskType(TaskType taskType);

    public List<TaskType> getAllTaskTypes();

    public TaskType getSingleTaskType(int id);

    public TaskType updateTaskType(int id, TaskType tasktype);

    public void deleteTaskType(int id);
}
