package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.TaskType;

public interface TaskTypeRepository extends JpaRepository<TaskType, Integer> {

}
