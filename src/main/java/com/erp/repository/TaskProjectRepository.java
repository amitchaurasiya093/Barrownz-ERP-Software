package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.TaskProject;

public interface TaskProjectRepository extends JpaRepository<TaskProject, Integer> {

}
