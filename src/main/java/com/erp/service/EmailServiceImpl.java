package com.erp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.erp.model.Employee;
import com.erp.model.LeaveApplication;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendLeaveAppliedEmail(LeaveApplication leave) {
        try {
            List<String> toList = new ArrayList<>();
            if (leave.getApplyTo() != null) {
                toList.add(leave.getApplyTo().getEmail());
            }

            List<String> ccList = leave.getCcEmployees()
                    .stream()
                    .map(Employee::getEmail)
                    .toList();

            String subject = "New Leave Application submitted";
            String body = buildLeaveAppliedBody(leave);

            sendMail(toList, ccList, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLeaveStatusUpdateEmail(LeaveApplication leave) {

        try {
            String subject = "Leave Application " + leave.getStatus();
            String body = buildLeaveStatusBody(leave);

            sendMail(Collections.singletonList(leave.getEmployee().getEmail()),
                    Collections.emptyList(),
                    subject,
                    body);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMail(List<String> to, List<String> cc, String subject, String body) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to.toArray(new String[0]));

        if (!cc.isEmpty()) {
            helper.setCc(cc.toArray(new String[0]));
        }

        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }

    private String buildLeaveAppliedBody(LeaveApplication leave) {
        return "<p>Hi " + leave.getApplyTo().getFirstName() + ",</p>" +
                "<p>A new leave Application has been submitted by " + leave.getEmployee().getFirstName() + " "
                + leave.getEmployee().getLastName() + ".</p>" +
                "<p>Leave Type: " + leave.getLeaveType().getLeaveType() + "</p>" +
                "<p>From Date: " + leave.getFromDate() + "</p>" +
                "<p>To Date: " + leave.getToDate() + "</p>" +
                "<p>No of Days: " + leave.getLeaveQuantity() + "</p>" +
                "<p>Reason: " + leave.getReason() + "</p>" +
                "<p>From Session: " + leave.getSessionFrom() + "</p>" +
                "<p>To Session: " + leave.getSessionTo() + "</p>" +
                "<p>Regards,<br/>Note: This is an auto-generated mail. Please do not reply.</p>";
    }

    private String buildLeaveStatusBody(LeaveApplication leave) {
        String approverName = leave.getActionBy() != null
                ? leave.getActionBy().getFirstName() + " " + leave.getActionBy().getLastName()
                : "System";

        return "<p>Hi " + leave.getEmployee().getFirstName() + ",</p>" +
                "<p>The leave application has been " + leave.getStatus() + " by " + approverName +
                "<p>Remark: " + leave.getReason() + "</p>" +
                "<p>Leave Type: " + leave.getLeaveType().getLeaveType() + "</p>" +
                "<p>From Date: " + leave.getFromDate() + "</p>" +
                "<p>To Date: " + leave.getToDate() + "</p>" +
                "<p>No of Days: " + leave.getLeaveQuantity() + "</p>" +
                "<p>Reason: " + leave.getReason() + "</p>" +
                "<p>From Session: " + leave.getSessionFrom() + "</p>" +
                "<p>To Session: " + leave.getSessionTo() + "</p>" +
                "<p>Regards, <br/>Note: This is an auto-generated mail. Please do not reply.</p>";

    }

}
