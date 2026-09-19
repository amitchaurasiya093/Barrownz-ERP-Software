package com.erp.service;

import java.util.List;

import com.erp.model.Department;

public interface DepartmentService {

    public Department createDepartment(Department department);

    public List<Department> getAllDepartments();

    public Department getDepartmentById(int id);

    public Department updateDepartment(int id, Department department);

    public void deleteDepartment(int id);
}
