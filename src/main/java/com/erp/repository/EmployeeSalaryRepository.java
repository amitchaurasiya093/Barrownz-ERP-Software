package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.EmployeeSalary;

public interface EmployeeSalaryRepository extends JpaRepository<EmployeeSalary, Integer> {

    EmployeeSalary findByEmployee_EmpId(int empId);
}
