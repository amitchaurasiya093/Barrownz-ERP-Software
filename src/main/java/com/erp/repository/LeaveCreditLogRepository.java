package com.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.LeaveCreditLog;

public interface LeaveCreditLogRepository extends JpaRepository<LeaveCreditLog, Integer> {

    Optional<LeaveCreditLog> findByCreditMonthAndCreditYear(int creditMonth, int creditYear);

}
