package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Department;
import com.erp.model.ReportingManager;
import com.erp.repository.DepartmentRepository;
import com.erp.repository.ReportingManagerRepository;

@Service
public class ReportingManagerImpl implements ReportingManagerService {

    @Autowired
    private ReportingManagerRepository rmRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    @Override
    public ReportingManager createReportingManager(ReportingManager manager) {
        return rmRepo.save(manager);
    }

    @Override
    public List<ReportingManager> getAllReportingManagers() {
        List<ReportingManager> managers = rmRepo.findAll();
        return managers;
    }

    @Override
    public ReportingManager getReportingManager(int id) {
        return rmRepo.findById(id).orElseThrow(() -> new RuntimeException("rm id not found: " + id));
    }

    @Override
    public ReportingManager updateReportingManager(ReportingManager rm, int id) {
        ReportingManager existingRM = rmRepo.findById(id).orElseThrow(() -> new RuntimeException("id not found" + id));

        existingRM.setRm_name1(rm.getRm_name1());
        existingRM.setRm_name2(rm.getRm_name2());

        if (rm.getDepartment() != null && rm.getDepartment().getDeptId() != 0) {
            Department updatedDepartment = departmentRepo.findById(rm.getDepartment().getDeptId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            existingRM.setDepartment(updatedDepartment);
        }

        ReportingManager updatedRM = rmRepo.save(existingRM);

        return updatedRM;
    }

    @Override
    public void deleteReportingManager(int id) {
        rmRepo.deleteById(id);
    }

    @Override
    public List<ReportingManager> getReportingManagersByDepartmentId(int deptId) {
        List<ReportingManager> allManagers = rmRepo.findByDepartment_DeptId(deptId);
        return allManagers;
    }

}
