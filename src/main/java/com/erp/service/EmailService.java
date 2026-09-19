package com.erp.service;

import com.erp.model.LeaveApplication;

public interface EmailService {

    public void sendLeaveAppliedEmail(LeaveApplication leave);

    public void sendLeaveStatusUpdateEmail(LeaveApplication leave);

}
