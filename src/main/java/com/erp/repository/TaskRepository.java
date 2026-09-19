package com.erp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.AddTask;

public interface TaskRepository extends JpaRepository<AddTask, Integer> {

    // method to show loggedin employee their task only and on that particular day
    List<AddTask> findByEmployeeEmpIdAndStartDateBetween(int empId, LocalDateTime start, LocalDateTime end);

}
