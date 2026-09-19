package com.erp.service;

import java.util.List;

import com.erp.dto.EmployeeSalaryDto;
import com.erp.model.EmployeeSalary;

public interface EmployeeSalaryService {

    public EmployeeSalaryDto saveEmployeeSalary(EmployeeSalaryDto salary);

    public List<EmployeeSalaryDto> getAllSalary();

    public EmployeeSalaryDto getSingleSalary(int id);

    public EmployeeSalaryDto updateSalary(int id, EmployeeSalaryDto dto);

    public void deleteSalary(int id);

    // get salary by employee Id
    public EmployeeSalaryDto getSalaryByEmployeeId(int empId);

}
