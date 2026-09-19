package com.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.ReportingManager;

public interface ReportingManagerRepository extends JpaRepository<ReportingManager, Integer> {

    List<ReportingManager> findByDepartment_DeptId(int deptId);
}
