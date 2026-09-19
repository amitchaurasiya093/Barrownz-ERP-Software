package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

}
