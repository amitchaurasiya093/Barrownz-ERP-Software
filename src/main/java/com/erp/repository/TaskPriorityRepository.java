package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.TaskPriority;

public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Integer> {

}
