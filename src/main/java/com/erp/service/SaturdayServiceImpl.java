package com.erp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.model.Saturday;
import com.erp.repository.SaturdayRepository;

@Service
public class SaturdayServiceImpl implements SaturdayService {

    private SaturdayRepository saturdayRepo;

    public SaturdayServiceImpl(SaturdayRepository saturdayRepo) {
        this.saturdayRepo = saturdayRepo;
    }

    @Override
    public Saturday saveSaturday(Saturday saturday) {
        LocalDate date = saturday.getDate();
        if (date == null)
            throw new IllegalArgumentException("Date cannot be null");
        if (saturdayRepo.existsByDate(date)) {
            throw new IllegalArgumentException("Saturday already exists for date: " + date);
        }
        return saturdayRepo.save(saturday);
    }

    @Override
    public List<Saturday> findAllSaturdays() {
        return saturdayRepo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        saturdayRepo.deleteById(id);
    }

    @Override
    public Saturday updateSaturday(Long id, Saturday saturday) {
        Saturday existing = saturdayRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Saturday not found with id: " + id));

        LocalDate newDate = saturday.getDate();
        if (newDate == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (saturdayRepo.existsByDate(newDate) && !existing.getDate().equals(newDate)) {
            throw new IllegalArgumentException("saturday already exists for date: " + newDate);
        }

        existing.setDate(newDate);
        existing.setMonth(saturday.getMonth());
        existing.setYear(saturday.getYear());

        return saturdayRepo.save(existing);

    }

}
