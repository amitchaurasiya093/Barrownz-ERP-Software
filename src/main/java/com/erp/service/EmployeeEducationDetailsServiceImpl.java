package com.erp.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.erp.dto.EmployeeEducationDetailsDto;
import com.erp.model.Employee;
import com.erp.model.EmployeeEducationDetails;
import com.erp.repository.EmployeeEducationDetailsRepository;
import com.erp.repository.EmployeeRepository;

@Service
public class EmployeeEducationDetailsServiceImpl implements EmployeeEducationDetailsService {

    private final EmployeeEducationDetailsRepository educationRepo;
    private final EmployeeRepository employeeRepo;

    public EmployeeEducationDetailsServiceImpl(EmployeeEducationDetailsRepository educationRepo,
            EmployeeRepository employeeRepo) {
        this.educationRepo = educationRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EmployeeEducationDetails saveEducation(EmployeeEducationDetailsDto dto) {
        Employee employee = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + dto.getEmployeeId()));

        List<EmployeeEducationDetails> existing = educationRepo.findByEmployee(employee);

        if (existing.size() >= 3) {
            throw new RuntimeException("Maximum 3 education allowed");
        }

        EmployeeEducationDetails details = new EmployeeEducationDetails();
        details.setQualification(dto.getQualification());
        details.setUniversity(dto.getUniversity());
        details.setRoleNumber(dto.getRoleNumber());
        details.setSubject(dto.getSubject());
        details.setPassingYear(dto.getPassingYear());
        details.setMarks(dto.getMarks());
        details.setEmployee(employee);

        return educationRepo.save(details);
    }

    @Override
    public EmployeeEducationDetails updateEducation(int id, EmployeeEducationDetailsDto dto) {
        EmployeeEducationDetails details = educationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Education record not found: " + id));

        details.setQualification(dto.getQualification());
        details.setUniversity(dto.getUniversity());
        details.setRoleNumber(dto.getRoleNumber());
        details.setSubject(dto.getSubject());
        details.setPassingYear(dto.getPassingYear());
        details.setMarks(dto.getMarks());

        return educationRepo.save(details);

    }

    @Override
    public void deleteEducation(int id) {
        EmployeeEducationDetails details = educationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Education record not found: " + id));

        educationRepo.delete(details);

    }

    @Override
    public List<EmployeeEducationDetails> findByEmployee(Employee employee) {
        return educationRepo.findByEmployee(employee);
    }

    @Override
    public List<EmployeeEducationDetails> findByEmployeeId(int employeeId) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));

        return educationRepo.findByEmployee(employee);
    }

}
