package com.erp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Employee;
import com.erp.model.EmployeeEducationDetails;

public interface EmployeeEducationDetailsRepository extends JpaRepository<EmployeeEducationDetails, Integer> {

    List<EmployeeEducationDetails> findByEmployee(Employee employee);

}
