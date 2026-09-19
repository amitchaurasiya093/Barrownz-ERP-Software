package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.TaskType;
import com.erp.repository.TaskTypeRepository;

@Service
public class TaskTypeServiceImpl implements TaskTypeService {

    @Autowired
    private TaskTypeRepository typeRepo;

    @Override
    public TaskType saveTaskType(TaskType taskType) {
        return typeRepo.save(taskType);
    }

    @Override
    public List<TaskType> getAllTaskTypes() {
        return typeRepo.findAll();
    }

    @Override
    public TaskType getSingleTaskType(int id) {
        return typeRepo.findById(id).orElseThrow(() -> new RuntimeException("type id not found: " + id));
    }

    @Override
    public TaskType updateTaskType(int id, TaskType tasktype) {
        TaskType existingTaskType = typeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("type id not found: " + id));

        existingTaskType.setTypeName(tasktype.getTypeName());
        TaskType updatedType = typeRepo.save(existingTaskType);
        return updatedType;
    }

    @Override
    public void deleteTaskType(int id) {
        typeRepo.deleteById(id);
    }

}
