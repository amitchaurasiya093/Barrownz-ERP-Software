package com.erp.scheduler;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.erp.model.LeaveCreditLog;
import com.erp.repository.LeaveCreditLogRepository;
import com.erp.service.LeaveBalanceService;

import jakarta.annotation.PostConstruct;

@Service
public class LeaveBalanceScheduler {

    private LeaveBalanceService leaveBalanceService;
    private LeaveCreditLogRepository creditLogRepo;

    public LeaveBalanceScheduler(LeaveBalanceService leaveBalanceService, LeaveCreditLogRepository creditLogRepo) {
        this.leaveBalanceService = leaveBalanceService;
        this.creditLogRepo = creditLogRepo;
    }

    @PostConstruct
    public void runOnStartup() {
        System.out.println("Running leave balance credit check at startup...");
        leaveBalanceService.creditMonthlyLeaveForAll();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void creditMonthlyLeave() {
        leaveBalanceService.creditMonthlyLeaveForAll();
    }

    // run at midnight on first day of every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public void sheduledCredit() {
        creditIfNotAlready();
    }

    // run once at application start up
    @PostConstruct
    public void initCredit() {
        creditIfNotAlready();
    }

    public void creditIfNotAlready() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        boolean alreadyCredited = creditLogRepo.findByCreditMonthAndCreditYear(month, year).isPresent();

        if (alreadyCredited) {
            System.out.println("Monthly leave already credited for " + month + "/" + year);
            return;
        }

        // Credit all employees
        leaveBalanceService.creditMonthlyLeaveForAll();

        // Save a log entry
        LeaveCreditLog log = new LeaveCreditLog();
        log.setCreditMonth(month);
        log.setCreditYear(year);
        creditLogRepo.save(log);

        System.out.println("Monthly leave credited for " + month + "/" + year);
    }
}
