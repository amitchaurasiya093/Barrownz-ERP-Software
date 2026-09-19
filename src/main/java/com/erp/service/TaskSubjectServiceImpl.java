package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.TaskSubject;
import com.erp.repository.TaskSubjectRepository;

@Service
public class TaskSubjectServiceImpl implements TaskSubjectService {

    @Autowired
    private TaskSubjectRepository subjectRepo;

    @Override
    public TaskSubject saveTaskSubject(TaskSubject taskSubject) {
        return subjectRepo.save(taskSubject);
    }

    @Override
    public List<TaskSubject> getAllTaskSubjects() {
        return subjectRepo.findAll();
    }

    @Override
    public TaskSubject getSingleTaskSubject(int id) {
        return subjectRepo.findById(id).orElseThrow(() -> new RuntimeException("subject id not found " + id));
    }

    @Override
    public TaskSubject updateTaskSubject(int id, TaskSubject taskSubject) {
        TaskSubject existingTaskSubject = subjectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("subject id not found: " + id));

        existingTaskSubject.setSubjectName(taskSubject.getSubjectName());

        TaskSubject updatedTaskSubject = subjectRepo.save(existingTaskSubject);
        return updatedTaskSubject;

    }

    @Override
    public void deleteTaskSubject(int id) {
        subjectRepo.deleteById(id);
    }

}
