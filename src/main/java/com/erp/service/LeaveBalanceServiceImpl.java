package com.erp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.model.Employee;
import com.erp.model.LeaveBalance;
import com.erp.model.LeaveType;
import com.erp.repository.LeaveBalanceRepository;
import com.erp.repository.LeaveTypeRepository;
import com.erp.service.LeaveBalanceService;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveTypeRepository leaveTypeRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;

    public LeaveBalanceServiceImpl(LeaveTypeRepository leaveTypeRepo, LeaveBalanceRepository leaveBalanceRepo) {
        this.leaveTypeRepo = leaveTypeRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
    }

    public void initializeLeaveBalance(Employee emp) {
        List<LeaveType> leaveTypes = leaveTypeRepo.findAll();

        for (LeaveType type : leaveTypes) {
            if (type.getAllowedForProbation() || "confirmed".equalsIgnoreCase(emp.getJoining_status())) {
                LeaveBalance balance = new LeaveBalance();
                balance.setEmployee(emp);
                balance.setLeaveType(type);
                balance.setBalance(0);
                balance.setTotalAllocated(0);
                leaveBalanceRepo.save(balance);
            }
        }
    }

    @Override
    public List<LeaveBalance> getBalancesForEmployee(int empID) {
        return leaveBalanceRepo.findByEmployeeEmpId(empID);
    }

    @Override
    public void creditMonthlyLeaveForEmployee(int empId) {
        List<LeaveBalance> balances = leaveBalanceRepo.findByEmployeeEmpId(empId);
        for (LeaveBalance balance : balances) {
            Employee emp = balance.getEmployee();
            LeaveType type = balance.getLeaveType();

            // Only credit Paid Leave (PPL)
            // if (type.getLeavetypeId() == 5 &&
            // emp.getJoining_status().equalsIgnoreCase("confirmed")) {
            // LocalDateTime lastUpdate = balance.getUpdatedAt() != null ?
            // balance.getUpdatedAt()
            // : balance.getDateCreated();

            // long monthsElapsed = ChronoUnit.MONTHS.between(
            // lastUpdate.toLocalDate().withDayOfMonth(1),
            // LocalDate.now().withDayOfMonth(1));

            // if (monthsElapsed > 0) {
            // double monthlyCredit = 1.5;
            // double totalCredit = monthlyCredit * monthsElapsed;

            // balance.setBalance(balance.getBalance() + totalCredit);
            // balance.setTotalAllocated(balance.getTotalAllocated() + totalCredit);
            // balance.setUpdatedAt(LocalDateTime.now());

            // leaveBalanceRepo.save(balance);
            // }
            // }

            if (type.getRequiresBalance() != null && type.getRequiresBalance()
                    && emp.getEmployee_status().equalsIgnoreCase("confirm")) {

                LocalDateTime lastUpdate = balance.getUpdatedAt() != null ? balance.getUpdatedAt()
                        : balance.getDateCreated();

                long monthsElapsed = ChronoUnit.MONTHS.between(
                        lastUpdate.toLocalDate().withDayOfMonth(1),
                        LocalDate.now().withDayOfMonth(1));

                if (monthsElapsed > 0) {
                    double monthlyCredit = 1.5;
                    double totalCredit = monthlyCredit * monthsElapsed;

                    balance.setBalance(balance.getBalance() + totalCredit);
                    balance.setTotalAllocated(balance.getTotalAllocated() + totalCredit);
                    balance.setUpdatedAt(LocalDateTime.now());

                    leaveBalanceRepo.save(balance);
                }
            }

        }
    }

    @Override
    public void creditMonthlyLeaveForAll() {
        List<LeaveBalance> allBalances = leaveBalanceRepo.findAll();
        for (LeaveBalance balance : allBalances) {
            Employee emp = balance.getEmployee();
            LeaveType type = balance.getLeaveType();

            // Credit only leave type that requires balance (ppl)
            if (type.getRequiresBalance() != null && type.getRequiresBalance()
                    && emp.getEmployee_status().equalsIgnoreCase("confirm")) {

                LocalDateTime lastUpdate = balance.getUpdatedAt() != null ? balance.getUpdatedAt()
                        : balance.getDateCreated();

                long monthsElapsed = ChronoUnit.MONTHS.between(
                        lastUpdate.toLocalDate().withDayOfMonth(1),
                        LocalDate.now().withDayOfMonth(1));

                // debug print
                System.out.println("Emp=" + emp.getEmpId() +
                        ", Status='" + emp.getEmployee_status() + "'" +
                        ", LeaveType=" + type.getLeaveType() +
                        ", RequiresBalance=" + type.getRequiresBalance() +
                        ", MonthsElapsed=" + monthsElapsed);

                if (monthsElapsed > 0) {
                    double monthlyCredit = 1.5;
                    double totalCredit = monthlyCredit * monthsElapsed;

                    balance.setBalance(balance.getBalance() + totalCredit);
                    balance.setTotalAllocated(balance.getTotalAllocated() + totalCredit);
                    balance.setUpdatedAt(LocalDateTime.now());

                    leaveBalanceRepo.save(balance);
                }

            }
        }

    }
}
