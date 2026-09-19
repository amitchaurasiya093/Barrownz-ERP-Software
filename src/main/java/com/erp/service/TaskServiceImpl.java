package com.erp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.dto.TaskRequestDto;
import com.erp.dto.TaskResponseDto;
import com.erp.model.AddTask;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.TaskPriorityRepository;
import com.erp.repository.TaskProjectRepository;
import com.erp.repository.TaskRepository;
import com.erp.repository.TaskStatusRepository;
import com.erp.repository.TaskSubjectRepository;
import com.erp.repository.TaskTypeRepository;
import com.erp.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepo;
    private TaskTypeRepository taskTypeRepo;
    private TaskPriorityRepository taskPriorityRepo;
    private TaskStatusRepository taskStatusRepo;
    private TaskSubjectRepository taskSubjectRepo;
    private TaskProjectRepository taskProjectRepo;
    private EmployeeRepository employeeRepo;
    private JwtUtil jwtUtil;

    public TaskServiceImpl(TaskRepository taskRepo, TaskTypeRepository taskTypeRepo,
            TaskPriorityRepository taskPriorityRepo, TaskStatusRepository taskStatusRepo,
            TaskSubjectRepository taskSubjectRepo, TaskProjectRepository taskProjectRepo,
            EmployeeRepository employeeRepo, JwtUtil jwtUtil) {
        this.taskRepo = taskRepo;
        this.taskTypeRepo = taskTypeRepo;
        this.taskPriorityRepo = taskPriorityRepo;
        this.taskStatusRepo = taskStatusRepo;
        this.taskSubjectRepo = taskSubjectRepo;
        this.taskProjectRepo = taskProjectRepo;
        this.employeeRepo = employeeRepo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AddTask createTask(TaskRequestDto dto) {

        AddTask task = new AddTask();

        task.setTaskType(taskTypeRepo.findById(dto.getTypeId()).orElseThrow());
        task.setTaskPriority(taskPriorityRepo.findById(dto.getPriorityId()).orElseThrow());
        task.setTaskStatus(taskStatusRepo.findById(dto.getStatusId()).orElseThrow());
        task.setTaskSubject(taskSubjectRepo.findById(dto.getSubjectId()).orElseThrow());
        task.setTaskProject(taskProjectRepo.findById(dto.getProjectId()).orElseThrow());
        task.setEmployee(employeeRepo.findById(dto.getEmployeeId()).orElseThrow());

        task.setTaskQuantity(dto.getTaskQuantity());
        task.setTaskDescription(dto.getTaskDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());

        AddTask newTask = taskRepo.save(task);
        return newTask;
    }

    @Override
    public List<AddTask> getAllTasks() {

        return taskRepo.findAll();
    }

    @Override
    public AddTask updateTask(int id, TaskRequestDto dto) {

        taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found with Id: " + id));

        AddTask updatedTask = mapDtoToEntity(dto);

        updatedTask.setTaskId(id);
        return taskRepo.save(updatedTask);

    }

    @Override
    public void deleteTask(int id) {
        AddTask task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("task not found with id: " + id));

        taskRepo.delete(task);

    }

    @Override
    public AddTask getTaskById(int id) {
        AddTask task = taskRepo.findById(id).orElseThrow(() -> new RuntimeException("task not found with Id: " + id));
        return task;
    }

    private AddTask mapDtoToEntity(TaskRequestDto dto) {
        AddTask task = new AddTask();

        task.setTaskType(taskTypeRepo.findById(dto.getTypeId()).orElseThrow());
        task.setTaskPriority(taskPriorityRepo.findById(dto.getPriorityId()).orElseThrow());
        task.setTaskStatus(taskStatusRepo.findById(dto.getStatusId()).orElseThrow());
        task.setTaskSubject(taskSubjectRepo.findById(dto.getSubjectId()).orElseThrow());
        task.setTaskProject(taskProjectRepo.findById(dto.getProjectId()).orElseThrow());
        task.setEmployee(employeeRepo.findById(dto.getEmployeeId()).orElseThrow());

        task.setTaskQuantity(dto.getTaskQuantity());
        task.setTaskDescription(dto.getTaskDescription());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());

        return task;
    }

    @Override
    public List<AddTask> getTasksForEmployeeByDate(int empId, LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return taskRepo.findByEmployeeEmpIdAndStartDateBetween(empId, startOfDay, endOfDay);

    }

    @Override
    public List<TaskResponseDto> getTodayTasksByLoggedInEmployee(HttpServletRequest request) {
        int empId = jwtUtil.extractEmpIdFromRequest(request);

        LocalDate today = LocalDate.now();
        List<AddTask> tasks = getTasksForEmployeeByDate(empId, today);

        return tasks.stream().map(task -> {
            TaskResponseDto dto = new TaskResponseDto();

            dto.setTaskId(task.getTaskId());
            dto.setProjectName(task.getTaskProject().getProjectName());
            dto.setSubjectName(task.getTaskSubject().getSubjectName());
            dto.setTypeName(task.getTaskType().getTypeName());
            dto.setPriorityName(task.getTaskPriority().getPriorityName());
            dto.setStatusName(task.getTaskStatus().getStatusName());
            dto.setStartDate(task.getStartDate());
            dto.setEndDate(task.getEndDate());
            dto.setTaskQuantity(task.getTaskQuantity());
            dto.setTaskDescription(task.getTaskDescription());
            return dto;

        }).toList();
    }

}
