package com.erp.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.TaskRequestDto;
import com.erp.dto.TaskResponseDto;
import com.erp.model.AddTask;
import com.erp.repository.TaskRepository;
import com.erp.service.TaskService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/addTask")
@CrossOrigin("*")
public class TaskRestController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepo;

    @PostMapping
    public ResponseEntity<AddTask> createTask(@RequestBody TaskRequestDto dto) {
        AddTask createdTask = taskService.createTask(dto);
        return new ResponseEntity<>(createdTask, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AddTask>> showAllTasks() {
        List<AddTask> allTasks = taskService.getAllTasks();
        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddTask> getTaskById(@PathVariable int id) {
        AddTask task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddTask> updatedTask(@PathVariable int id, @RequestBody TaskRequestDto dto) {
        AddTask updateAddTask = taskService.updateTask(id, dto);
        return new ResponseEntity<>(updateAddTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("task deleted successfully", HttpStatus.GONE);
    }

    @GetMapping("/by-employee-today")
    public ResponseEntity<List<TaskResponseDto>> getTodayTasksForLoggedInEmployee(HttpServletRequest request) {
        List<TaskResponseDto> tasks = taskService.getTodayTasksByLoggedInEmployee(request);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

}
