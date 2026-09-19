package com.erp.service;

import java.util.List;
import java.util.Optional;

import com.erp.model.Employee;
import com.erp.model.EmployeePersonalInfo;

public interface EmployeePersonalInfoService {

    public EmployeePersonalInfo savePersonalInfo(EmployeePersonalInfo info);

    public List<EmployeePersonalInfo> findAllInfo();

    public EmployeePersonalInfo findById(int id);

    public void deleteInfo(int id);

    Optional<EmployeePersonalInfo> findByEmployee(Employee employee);

}
