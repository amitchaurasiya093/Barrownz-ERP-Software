package com.erp.service;

import java.util.List;

import com.erp.dto.EmployeeEducationDetailsDto;
import com.erp.model.Employee;
import com.erp.model.EmployeeEducationDetails;

public interface EmployeeEducationDetailsService {

    EmployeeEducationDetails saveEducation(EmployeeEducationDetailsDto dto);

    EmployeeEducationDetails updateEducation(int id, EmployeeEducationDetailsDto dto);

    void deleteEducation(int id);

    List<EmployeeEducationDetails> findByEmployee(Employee employee);

    List<EmployeeEducationDetails> findByEmployeeId(int employeeId);

}
