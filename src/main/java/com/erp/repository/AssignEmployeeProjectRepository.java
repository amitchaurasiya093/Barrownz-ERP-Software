package com.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erp.model.AssignEmployeeProject;

public interface AssignEmployeeProjectRepository extends JpaRepository<AssignEmployeeProject, Integer> {

    // emp_id is not camel case → So use JPQL query
    @Query("SELECT a FROM AssignEmployeeProject a WHERE a.employee.empId = :empId")
    List<AssignEmployeeProject> findByEmployeeId(int empId);

}
