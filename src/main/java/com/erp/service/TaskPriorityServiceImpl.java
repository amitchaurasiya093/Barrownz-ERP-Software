package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.TaskPriority;
import com.erp.repository.TaskPriorityRepository;

@Service
public class TaskPriorityServiceImpl implements TaskPriorityService {

    @Autowired
    private TaskPriorityRepository priorityRepo;

    @Override
    public TaskPriority saveTaskPriority(TaskPriority taskPriority) {
        return priorityRepo.save(taskPriority);
    }

    @Override
    public List<TaskPriority> getAllTaskPriority() {
        return priorityRepo.findAll();
    }

    @Override
    public TaskPriority getSingleTaskPriority(int id) {
        return priorityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task priority not found: " + id));
    }

    @Override
    public TaskPriority updateTaskPriority(int id, TaskPriority taskPriority) {
        TaskPriority existingPriority = priorityRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task priority not found: " + id));

        existingPriority.setPriorityName(taskPriority.getPriorityName());

        return priorityRepo.save(existingPriority);
    }

    @Override
    public void deleteTaskPriority(int id) {
        priorityRepo.deleteById(id);
    }

}
