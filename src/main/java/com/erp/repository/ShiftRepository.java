package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {

}
