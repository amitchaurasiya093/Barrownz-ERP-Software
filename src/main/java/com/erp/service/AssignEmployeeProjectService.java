package com.erp.service;

import java.util.List;

import com.erp.model.AssignEmployeeProject;

public interface AssignEmployeeProjectService {

    List<AssignEmployeeProject> getAll();

    AssignEmployeeProject save(AssignEmployeeProject assign);

    AssignEmployeeProject update(int id, AssignEmployeeProject assign);

    void delete(int id);

    List<AssignEmployeeProject> getByEmployeeId(int empId); // extra method for filtering
}
