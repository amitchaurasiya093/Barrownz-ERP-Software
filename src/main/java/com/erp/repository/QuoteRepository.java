package com.erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.model.Quote;

public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    List<Quote> findAllByOrderByDateCreatedDesc();

    Quote findTopByOrderByDateCreatedDesc();
}
