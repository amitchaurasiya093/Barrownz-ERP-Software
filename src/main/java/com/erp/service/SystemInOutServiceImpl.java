package com.erp.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.erp.dto.SystemInOutDto;
import com.erp.mapper.SystemInOutMapper;
import com.erp.model.Employee;
import com.erp.model.SystemInOut;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.SystemInOutRepository;

@Service
public class SystemInOutServiceImpl implements SystemInOutService {

    private SystemInOutRepository systemRepo;
    private EmployeeRepository employeeRepo;

    public SystemInOutServiceImpl(SystemInOutRepository systemRepo, EmployeeRepository employeeRepo) {
        this.systemRepo = systemRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public SystemInOutDto marksSystemIn(int employeeId) {
        Optional<SystemInOut> latestRecord = systemRepo.findTopByEmployee_EmpIdOrderBySystemInOutIdDesc(employeeId);

        if (latestRecord.isPresent() && latestRecord.get().getSystemOut() == null) {
            throw new RuntimeException("Already marked System In. Please mark OUT first.");
        }

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        SystemInOut record = new SystemInOut();
        record.setEmployee(employee);
        // record.setSystemIn(LocalDateTime.now());
        record.setSystemIn(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime());

        SystemInOut saved = systemRepo.save(record);
        return SystemInOutMapper.convertToDto(saved);
    }

    @Override
    public SystemInOutDto marksSystemOut(int employeeId) {
        SystemInOut latestRecord = systemRepo.findTopByEmployee_EmpIdOrderBySystemInOutIdDesc(employeeId)
                .orElseThrow(() -> new RuntimeException("No System In record found"));

        if (latestRecord.getSystemOut() != null) {
            throw new RuntimeException("Already marked System OUT. Please mark IN again.");
        }

        // latestRecord.setSystemOut(LocalDateTime.now());
        latestRecord.setSystemOut(ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).toLocalDateTime());
        SystemInOut updated = systemRepo.save(latestRecord);
        return SystemInOutMapper.convertToDto(updated);
    }

    @Override
    public String getCurrentStatus(int employeeId) {

        Optional<SystemInOut> latestRecord = systemRepo.findTopByEmployee_EmpIdOrderBySystemInOutIdDesc(employeeId);

        if (latestRecord.isPresent() && latestRecord.get().getSystemOut() == null) {
            return "IN";
        }
        return "OUT";

    }

}
