package com.erp.service;

import java.util.List;

import com.erp.model.TaskProject;

public interface TaskProjectService {

    public TaskProject saveTaskProject(TaskProject taskProject);

    public List<TaskProject> getAllTaskProjects();

    public TaskProject getSingleTaskProject(int id);

    public TaskProject updateTaskProject(int id, TaskProject taskProject);

    public void deleteTaskProject(int id);

}
