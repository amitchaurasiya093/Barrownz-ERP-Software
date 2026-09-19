package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.LeaveType;
import com.erp.repository.LeaveTypeRepository;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leavetypeRepo;

    @Override
    public LeaveType createLeaveType(LeaveType leavetype) {
        return leavetypeRepo.save(leavetype);
    }

    @Override
    public LeaveType getLeaveTypeById(int id) {
        return leavetypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));
    }

    @Override
    public LeaveType updateLeaveType(int id, LeaveType leavetype) {
        LeaveType existingLeaveType = leavetypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("leavetype not found with id: " + id));

        existingLeaveType.setLeaveType(leavetype.getLeaveType());
        existingLeaveType.setAllowedForProbation(leavetype.getAllowedForProbation());
        existingLeaveType.setRequiresBalance(leavetype.getRequiresBalance());

        LeaveType updatedLeaveType = leavetypeRepo.save(existingLeaveType);
        return updatedLeaveType;

    }

    @Override
    public void deleteLeaveType(int id) {
        leavetypeRepo.deleteById(id);
    }

    @Override
    public List<LeaveType> getAllLeavetypes() {
        return leavetypeRepo.findAll();
    }

}
