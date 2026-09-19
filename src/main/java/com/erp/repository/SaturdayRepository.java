package com.erp.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Saturday;

public interface SaturdayRepository extends JpaRepository<Saturday, Long> {
    boolean existsByDate(LocalDate date);
}
