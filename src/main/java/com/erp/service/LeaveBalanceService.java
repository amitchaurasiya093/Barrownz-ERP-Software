package com.erp.service;

import java.util.List;
import com.erp.model.Employee;
import com.erp.model.LeaveBalance;

public interface LeaveBalanceService {

    public void initializeLeaveBalance(Employee emp);

    List<LeaveBalance> getBalancesForEmployee(int empID);

    // credit leave for a single employee
    public void creditMonthlyLeaveForEmployee(int empId);

    // credit leave for all employee
    public void creditMonthlyLeaveForAll();

}
