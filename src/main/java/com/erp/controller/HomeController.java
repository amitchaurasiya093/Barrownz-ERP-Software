package com.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Handlers for admin dashboard pages like, user-profile,
    // show-add-employee, show-add-department

    @GetMapping("/index")
    public String home() {
        return "dashboard/index";
    }

    @GetMapping("/user-profile")
    public String employeeProfile() {
        return "dashboard/app/user-profile";
    }

    @GetMapping("/show-add-employee")
    public String showAddEmployee() {
        return "dashboard/app/user-add";
    }

    @GetMapping("/show-add-department")
    public String showAddDepartment() {
        return "dashboard/app/department";
    }

    @GetMapping("/show-list-employee")
    public String showListEmployee() {
        return "dashboard/app/user-list";
    }

    @GetMapping("/show-leave")
    public String showLeaveType() {
        return "dashboard/app/leave-type";
    }

    @GetMapping("/show-shift")
    public String showShift() {
        return "dashboard/app/shift";
    }

    @GetMapping("/show-level")
    public String showLevel() {
        return "dashboard/app/add-level";
    }

    @GetMapping("/show-manager")
    public String showManager() {
        return "dashboard/app/r-manager";
    }

    @GetMapping("/show-project")
    public String showTaskProject() {
        return "dashboard/app/task-project";
    }

    @GetMapping("/show-subject")
    public String showTaskSubject() {
        return "dashboard/app/task-subject";
    }

    @GetMapping("/show-priority")
    public String showTaskPriority() {
        return "dashboard/app/task-priority";
    }

    @GetMapping("/show-type")
    public String showTaskType() {
        return "dashboard/app/task-type";
    }

    @GetMapping("/show-status")
    public String showTaskStatus() {
        return "dashboard/app/task-status";
    }

    @GetMapping("/show-assign-employee")
    public String showAssignEmployeeProject() {
        return "dashboard/app/assign-employee-project";
    }

    @GetMapping("/show-EmployeeSalary")
    public String showEmployeeSalary() {
        return "dashboard/app/employee-salary";
    }

    @GetMapping("/show-projectDetails")
    public String showProjectDetails() {
        return "dashboard/app/project-details";
    }

    @GetMapping("/show-client")
    public String showAddClient() {
        return "dashboard/app/client";
    }

    @GetMapping("/show-table")
    public String showTableDate() {
        return "dashboard/table/table-data";
    }

    @GetMapping("/showHoliday")
    public String showHoliday() {
        return "dashboard/app/holiday";
    }

    // employee dashboard show HolidayList

    @GetMapping("/showHolidayList")
    public String showHolidayList() {
        return "dashboard/app/employee-showHoliday";
    }

    @GetMapping("/show-login")
    public String showLoginPage() {
        return "dashboard/app/login";
    }

    // handler for showing HR dashboard after login using js
    @GetMapping("/dashboard/index.html")
    public String adminDashboard() {
        return "dashboard/index";
    }

    @GetMapping("/dashboard/employee-panel.html")
    public String employeePanel() {
        return "dashboard/employee-panel";
    }

    @GetMapping("/employee/dashboard/task")
    public String employeeTaskDashboard() {
        return "dashboard/app/employee-task-dashboard";
    }

    @GetMapping("/employee/dashboard/addTask")
    public String employeeAddTask() {
        return "dashboard/app/employee-addTask";
    }

    @GetMapping("/employee/dashboard/applyLeave")
    public String employeeApplyLeave() {
        return "dashboard/app/employee-applyLeave";
    }

    @GetMapping("/employee/dashboard/trackLeave")
    public String employeeTrackLeave() {
        return "dashboard/app/employee-trackLeave";
    }

    @GetMapping("/employee/dashboard/allLeave")
    public String employeeAllLeave() {
        return "dashboard/app/employee-allLeave";
    }

    @GetMapping("/employee/dashboard/viewInfo")
    public String viewEmployeeInfo() {
        return "dashboard/app/view-employee-info";
    }

    @GetMapping("/viewProfile")
    public String showEmployeeProfile() {
        return "dashboard/app/employee-profile";
    }

    // handler for showing landing page

    @GetMapping("/home")
    public String showLandingPage() {
        return "dashboard/app/home";
    }

    // admin-panel
    @GetMapping("/dashboard/admin-panel.html")
    public String showAdminDashboard() {
        return "dashboard/admin-panel";
    }

    @GetMapping("/viewSaturday")
    public String showSaturdays() {
        return "dashboard/app/add-saturday";
    }

    // @GetMapping("/signin")
    // public String showSignIn() {
    // return "dashboard/auth/sign-in";
    // }

    @GetMapping("/show-AllLeave")
    public String showHrAllLeaves() {
        return "dashboard/app/hr-allLeave";
    }
}
