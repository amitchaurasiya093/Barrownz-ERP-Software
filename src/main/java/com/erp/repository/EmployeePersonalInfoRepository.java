package com.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Employee;
import com.erp.model.EmployeePersonalInfo;

public interface EmployeePersonalInfoRepository extends JpaRepository<EmployeePersonalInfo, Integer> {
    Optional<EmployeePersonalInfo> findByEmployee(Employee employee);
}
