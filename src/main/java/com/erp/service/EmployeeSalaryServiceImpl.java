package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.dto.EmployeeSalaryDto;
import com.erp.model.Employee;
import com.erp.model.EmployeeSalary;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.EmployeeSalaryRepository;

@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {

        @Autowired
        private EmployeeSalaryRepository employeeSalaryRepo;

        @Autowired
        private EmployeeRepository employeeRepo;

        @Override
        public EmployeeSalaryDto saveEmployeeSalary(EmployeeSalaryDto dto) {
                // return employeeSalaryRepo.save(salary);

                Employee employee = employeeRepo.findById(dto.getEmpId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Employee not found with id: " + dto.getEmpId()));

                EmployeeSalary employeeSalary = new EmployeeSalary();
                employeeSalary.setEmployee(employee);
                employeeSalary.setMonthlySalary(dto.getMonthlySalary());

                EmployeeSalary saved = employeeSalaryRepo.save(employeeSalary);

                String employeeName = employee.getFirstName() + " " + employee.getLastName();

                return new EmployeeSalaryDto(saved.getSalaryId(), saved.getEmployee().getEmpId(),
                                saved.getMonthlySalary(), employeeName);

        }

        @Override
        public List<EmployeeSalaryDto> getAllSalary() {
                return employeeSalaryRepo.findAll().stream()
                                .map(s -> new EmployeeSalaryDto(s.getSalaryId(), s.getEmployee().getEmpId(),
                                                s.getMonthlySalary(),
                                                s.getEmployee().getFirstName() + " " + s.getEmployee().getLastName()))
                                .toList();

        }

        @Override
        public EmployeeSalaryDto getSingleSalary(int id) {
                EmployeeSalary salary = employeeSalaryRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("salary id not found :" + id));
                return new EmployeeSalaryDto(salary.getSalaryId(), salary.getEmployee().getEmpId(),
                                salary.getMonthlySalary(),
                                salary.getEmployee().getFirstName() + " " + salary.getEmployee().getLastName());
        }

        @Override
        public EmployeeSalaryDto updateSalary(int id, EmployeeSalaryDto dto) {
                EmployeeSalary existingSalary = employeeSalaryRepo.findById(id)
                                .orElseThrow(() -> new RuntimeException("salary id not found :" + id));

                Employee employee = employeeRepo.findById(dto.getEmpId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Employee not found with id: " + dto.getEmpId()));

                existingSalary.setMonthlySalary(dto.getMonthlySalary());
                existingSalary.setEmployee(employee);

                EmployeeSalary updated = employeeSalaryRepo.save(existingSalary);

                String employeeName = employee.getFirstName() + " " + employee.getLastName();

                return new EmployeeSalaryDto(updated.getSalaryId(), updated.getEmployee().getEmpId(),
                                updated.getMonthlySalary(), employeeName);
        }

        // existingSalary.setEmployee(salary.getEmployee());

        // EmployeeSalary updatedSalary = employeeSalaryRepo.save(existingSalary);
        // return updatedSalary;

        @Override
        public void deleteSalary(int id) {
                employeeSalaryRepo.deleteById(id);
        }

        @Override
        public EmployeeSalaryDto getSalaryByEmployeeId(int empId) {
                // return employeeSalaryRepo.findByEmployee_EmpId(empId);
                EmployeeSalary salary = employeeSalaryRepo.findByEmployee_EmpId(empId);
                if (salary == null) {
                        throw new RuntimeException("No salary found for employee id: " + empId);
                }
                return new EmployeeSalaryDto(salary.getSalaryId(), salary.getEmployee().getEmpId(),
                                salary.getMonthlySalary(),
                                salary.getEmployee().getFirstName() + " " + salary.getEmployee().getLastName());
        }
}
