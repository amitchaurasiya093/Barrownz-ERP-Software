package com.erp.service;

import java.util.List;

import com.erp.model.ReportingManager;

public interface ReportingManagerService {

    public ReportingManager createReportingManager(ReportingManager manager);

    public List<ReportingManager> getAllReportingManagers();

    public ReportingManager getReportingManager(int id);

    public ReportingManager updateReportingManager(ReportingManager rm, int id);

    public void deleteReportingManager(int id);

    public List<ReportingManager> getReportingManagersByDepartmentId(int deptId);
}
