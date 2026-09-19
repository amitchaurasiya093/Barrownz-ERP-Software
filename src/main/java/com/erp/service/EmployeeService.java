package com.erp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.erp.dto.EmployeeDto;
import com.erp.model.Employee;

public interface EmployeeService {

    // public Employee createEmployee(Employee employee, MultipartFile file) throws
    // IOException;
    public Employee createEmployee(EmployeeDto dto, MultipartFile file) throws IOException;

    // public List<Employee> showAllEmployees();
    public List<EmployeeDto> getAllEmployeesDto();

    public Employee getSingleEmployee(int id);

    // public Employee updateEmployee(Employee employee, int id, MultipartFile
    // file);
    public Employee updateEmployee(EmployeeDto dto, int id, MultipartFile file) throws IOException;

    public void deleteEmployee(int id);

    List<Employee> getEmployeesByDepartmentId(int deptId);

    // method for get Employee Code
    public Employee findByEmpCode(int empCode);

    // method for get Upcoming Birthdays
    public List<Employee> getBirthdaysInCurrentMonth();

    public default EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();

        dto.setEmpId(employee.getEmpId());
        dto.setEmp_code(employee.getEmpCode());
        dto.setFirst_name(employee.getFirstName());
        dto.setLast_name(employee.getLastName());

        return dto;

    }

    // method for new joinees
    List<Map<String, Object>> getNewJoinees();

}
