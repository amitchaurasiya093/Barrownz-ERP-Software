package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.TaskProject;
import com.erp.repository.TaskProjectRepository;

@Service
public class TaskProjectServiceImpl implements TaskProjectService {

    @Autowired
    private TaskProjectRepository projectRepo;

    @Override
    public TaskProject saveTaskProject(TaskProject taskProject) {
        return projectRepo.save(taskProject);
    }

    @Override
    public List<TaskProject> getAllTaskProjects() {
        List<TaskProject> allProjects = projectRepo.findAll();
        return allProjects;
    }

    @Override
    public TaskProject getSingleTaskProject(int id) {
        return projectRepo.findById(id).orElseThrow(() -> new RuntimeException("project not found :" + id));
    }

    @Override
    public TaskProject updateTaskProject(int id, TaskProject taskProject) {
        TaskProject existingProject = projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("project not found :" + id));

        existingProject.setProjectName(taskProject.getProjectName());
        TaskProject updatedProject = projectRepo.save(existingProject);
        return updatedProject;
    }

    @Override
    public void deleteTaskProject(int id) {
        projectRepo.deleteById(id);
    }

}
