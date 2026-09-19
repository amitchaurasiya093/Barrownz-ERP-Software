package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.TaskStatus;
import com.erp.repository.TaskStatusRepository;

@Service
public class TaskStatusServiceImpl implements TaskStatusService {

    @Autowired
    private TaskStatusRepository statusRepo;

    @Override
    public TaskStatus saveTaskStatus(TaskStatus taskStatus) {
        return statusRepo.save(taskStatus);
    }

    @Override
    public List<TaskStatus> getAllTaskStatus() {
        return statusRepo.findAll();
    }

    @Override
    public TaskStatus getSingleTaskStatus(int id) {
        return statusRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task status not found: " + id));
    }

    @Override
    public TaskStatus updateTaskStatus(int id, TaskStatus taskStatus) {
        TaskStatus existingStatus = statusRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task status not found: " + id));

        existingStatus.setStatusName(taskStatus.getStatusName());

        return statusRepo.save(existingStatus);
    }

    @Override
    public void deleteTaskStatus(int id) {
        statusRepo.deleteById(id);
    }
}
