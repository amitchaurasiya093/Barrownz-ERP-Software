package com.erp.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.erp.model.Employee;
import com.erp.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String empCodeStr) throws UsernameNotFoundException {
        int empCode = Integer.parseInt(empCodeStr);

        Employee employee = employeeRepo.findByEmpCode(empCode);
        if (employee == null) {
            throw new UsernameNotFoundException("Invalid employee code.");
        }

        String roleName = employee.getRole().getRole_name();

        System.out.println("Logged in with empCode: " + empCode + " | Role: " + roleName);

        // String springRole = "ROLE_" + roleName;

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(employee.getEmpCode()),
                employee.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(roleName)));
    }

}
