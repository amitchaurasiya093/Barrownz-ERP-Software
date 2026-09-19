package com.erp.service;

import java.time.LocalDate;
import java.util.List;

import com.erp.dto.TaskRequestDto;
import com.erp.dto.TaskResponseDto;
import com.erp.model.AddTask;

import jakarta.servlet.http.HttpServletRequest;

public interface TaskService {

    public AddTask createTask(TaskRequestDto dto);

    public List<AddTask> getAllTasks();

    public AddTask updateTask(int id, TaskRequestDto dto);

    public void deleteTask(int id);

    public AddTask getTaskById(int id);

    List<AddTask> getTasksForEmployeeByDate(int empId, LocalDate date);

    // method to return taskResponse
    List<TaskResponseDto> getTodayTasksByLoggedInEmployee(HttpServletRequest request);
}
