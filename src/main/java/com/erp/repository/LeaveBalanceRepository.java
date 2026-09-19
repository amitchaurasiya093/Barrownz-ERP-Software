package com.erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.LeaveBalance;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {

    Optional<LeaveBalance> findByEmployeeEmpIdAndLeaveTypeLeavetypeId(int empId, int leaveTypeId);

    List<LeaveBalance> findByEmployeeEmpId(int empId);
}
