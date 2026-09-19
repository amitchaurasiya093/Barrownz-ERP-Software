package com.erp;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.erp.model.Employee;
import com.erp.model.LeaveBalance;
import com.erp.model.LeaveType;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.LeaveBalanceRepository;
import com.erp.repository.LeaveTypeRepository;

// @Configuration
// public class LeaveBalanceInitializer {

// private final EmployeeRepository employeeRepo;
// private final LeaveTypeRepository leaveTypeRepo;
// private final LeaveBalanceRepository leaveBalanceRepo;

// public LeaveBalanceInitializer(EmployeeRepository employeeRepo,
// LeaveTypeRepository leaveTypeRepo,
// LeaveBalanceRepository leaveBalanceRepo) {
// this.employeeRepo = employeeRepo;
// this.leaveTypeRepo = leaveTypeRepo;
// this.leaveBalanceRepo = leaveBalanceRepo;
// }

// @Bean
// CommandLineRunner initLeaveBalances() {
// return args -> {
// // fetching leave type that has paid leave
// List<LeaveType> leaveTypeWithBalance = leaveTypeRepo.findAll()
// .stream()
// .filter(LeaveType::getRequiresBalance)
// .toList();

// employeeRepo.findAll().forEach(emp -> {
// for (LeaveType lt : leaveTypeWithBalance) {
// boolean alreadyExists = leaveBalanceRepo
// .findByEmployeeEmpIdAndLeaveTypeLeavetypeId(emp.getEmpId(),
// lt.getLeavetypeId())
// .isPresent();

// if (!alreadyExists) {
// LeaveBalance balance = new LeaveBalance();
// balance.setEmployee(emp);
// balance.setLeaveType(lt);
// balance.setBalance(0); // start with 0
// balance.setTotalAllocated(0); // nothing credited yet
// leaveBalanceRepo.save(balance);

// System.out.println("✅ PPL Leave balance initialized for employee: " +
// emp.getEmpCode());
// }
// }
// });
// };
// }

// @Bean
// CommandLineRunner topUpSingleEmployee() {
// return args -> {
// // Fetch the employee you want to top up
// Employee emp = employeeRepo.findById(7)
// .orElseThrow(() -> new RuntimeException("Employee not found"));

// // Fetch the leave type that requires balance
// LeaveType paidLeave = leaveTypeRepo.findAll()
// .stream()
// .filter(LeaveType::getRequiresBalance)
// .findFirst()
// .orElseThrow(() -> new RuntimeException("No leave type with balance found"));

// // Fetch existing leave balance or create a new one
// LeaveBalance balance = leaveBalanceRepo
// .findByEmployeeEmpIdAndLeaveTypeLeavetypeId(emp.getEmpId(),
// paidLeave.getLeavetypeId())
// .orElseGet(() -> {
// LeaveBalance b = new LeaveBalance();
// b.setEmployee(emp);
// b.setLeaveType(paidLeave);
// return b;
// });

// // Set your test balance
// balance.setBalance(3);
// balance.setTotalAllocated(3);
// leaveBalanceRepo.save(balance);

// System.out.println(
// "✅ Employee " + emp.getEmpCode() + " topped up with 3 days for " +
// paidLeave.getLeaveType());
// };
// }

// }
