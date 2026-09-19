package com.erp.service;

import java.util.List;

import com.erp.model.LeaveType;

public interface LeaveTypeService {

    public LeaveType createLeaveType(LeaveType leavetype);

    public List<LeaveType> getAllLeavetypes();

    public LeaveType getLeaveTypeById(int id);

    public LeaveType updateLeaveType(int id, LeaveType leavetype);

    public void deleteLeaveType(int id);

}
