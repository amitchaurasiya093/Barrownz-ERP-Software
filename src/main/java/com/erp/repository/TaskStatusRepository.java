package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.TaskStatus;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Integer> {

}
