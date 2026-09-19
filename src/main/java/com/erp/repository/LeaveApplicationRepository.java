package com.erp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.model.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {

    List<LeaveApplication> findByEmployeeEmpId(int empId);

    List<LeaveApplication> findByApplyToEmpId(int empId);

    // find by employee and month
    @Query("SELECT l FROM LeaveApplication l " +
            "WHERE l.employee.id = :empId " +
            "AND l.fromDate <= :endOfMonth " +
            "AND l.toDate >= :startOfMonth")
    public List<LeaveApplication> findByEmployeeAndMonth(
            @Param("empId") int empId,
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth);

}
