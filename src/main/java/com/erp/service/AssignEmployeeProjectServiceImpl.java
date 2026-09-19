package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.AssignEmployeeProject;
import com.erp.repository.AssignEmployeeProjectRepository;

@Service
public class AssignEmployeeProjectServiceImpl implements AssignEmployeeProjectService {

    @Autowired
    private AssignEmployeeProjectRepository repository;

    @Override
    public List<AssignEmployeeProject> getAll() {
        return repository.findAll();
    }

    @Override
    public AssignEmployeeProject save(AssignEmployeeProject assign) {
        return repository.save(assign);
    }

    @Override
    public AssignEmployeeProject update(int id, AssignEmployeeProject updated) {
        AssignEmployeeProject existing = repository.findById(id).orElse(null);
        if (existing != null) {
            existing.setEmployee(updated.getEmployee());
            existing.setEmpProjectName(updated.getEmpProjectName());
            return repository.save(existing);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<AssignEmployeeProject> getByEmployeeId(int empId) {
        return repository.findByEmployeeId(empId);
    }

}
