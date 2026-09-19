package com.erp.service;

import java.util.List;

import com.erp.model.TaskPriority;

public interface TaskPriorityService {

    TaskPriority saveTaskPriority(TaskPriority taskPriority);

    List<TaskPriority> getAllTaskPriority();

    TaskPriority getSingleTaskPriority(int id);

    TaskPriority updateTaskPriority(int id, TaskPriority taskPriority);

    void deleteTaskPriority(int id);

}
