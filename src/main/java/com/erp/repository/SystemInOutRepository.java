package com.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.SystemInOut;

public interface SystemInOutRepository extends JpaRepository<SystemInOut, Integer> {

    // getting the system in or out for an employee
    Optional<SystemInOut> findTopByEmployee_EmpIdOrderBySystemInOutIdDesc(int employeeId);

}
