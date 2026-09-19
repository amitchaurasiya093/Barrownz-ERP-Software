package com.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

}
