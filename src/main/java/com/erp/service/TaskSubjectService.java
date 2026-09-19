package com.erp.service;

import java.util.List;

import com.erp.model.TaskSubject;

public interface TaskSubjectService {

    public TaskSubject saveTaskSubject(TaskSubject taskSubject);

    public List<TaskSubject> getAllTaskSubjects();

    public TaskSubject getSingleTaskSubject(int id);

    public TaskSubject updateTaskSubject(int id, TaskSubject taskSubject);

    public void deleteTaskSubject(int id);

}
