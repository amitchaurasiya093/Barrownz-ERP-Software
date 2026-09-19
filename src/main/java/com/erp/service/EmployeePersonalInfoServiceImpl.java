package com.erp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Employee;
import com.erp.model.EmployeePersonalInfo;
import com.erp.repository.EmployeePersonalInfoRepository;

@Service
public class EmployeePersonalInfoServiceImpl implements EmployeePersonalInfoService {

    @Autowired
    private EmployeePersonalInfoRepository infoRepo;

    @Override
    public EmployeePersonalInfo savePersonalInfo(EmployeePersonalInfo info) {
        return infoRepo.save(info);
    }

    @Override
    public List<EmployeePersonalInfo> findAllInfo() {
        return infoRepo.findAll();
    }

    @Override
    public EmployeePersonalInfo findById(int id) {
        return infoRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteInfo(int id) {
        infoRepo.deleteById(id);
    }

    @Override
    public Optional<EmployeePersonalInfo> findByEmployee(Employee employee) {
        return infoRepo.findByEmployee(employee);
    }

}
