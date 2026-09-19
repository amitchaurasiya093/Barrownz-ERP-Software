package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.LeaveType;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {

}
