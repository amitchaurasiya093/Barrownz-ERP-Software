package com.erp.service;

import com.erp.dto.SystemInOutDto;

public interface SystemInOutService {

    public SystemInOutDto marksSystemIn(int employeeId);

    public SystemInOutDto marksSystemOut(int employeeId);

    public String getCurrentStatus(int employeeId);

}
