package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Department;
import com.erp.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepo;

    @Override
    public Department createDepartment(Department department) {
        return departmentRepo.save(department);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepo.findAll();
    }

    @Override
    public Department getDepartmentById(int id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("department not found with id: " + id));
    }

    @Override
    public Department updateDepartment(int id, Department department) {
        Department existingDepartment = departmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("department not found with id: " + id));

        existingDepartment.setDept_name(department.getDept_name());

        Department updatedDepartment = departmentRepo.save(existingDepartment);
        return updatedDepartment;

    }

    @Override
    public void deleteDepartment(int id) {
        departmentRepo.deleteById(id);
    }

}
