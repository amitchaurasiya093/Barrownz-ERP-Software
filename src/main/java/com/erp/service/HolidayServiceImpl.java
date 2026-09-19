package com.erp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Holiday;
import com.erp.repository.HolidayRepository;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Override
    public Holiday saveHoliday(Holiday holiday) {
        holiday.setStatus(calculateStatus(holiday.getDate()));
        return holidayRepository.save(holiday);
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    @Override
    public Holiday getHolidayById(Long id) {
        return holidayRepository.findById(id).orElse(null);
    }

    @Override
    public Holiday updateHoliday(Long id, Holiday updatedHoliday) {
        Holiday existing = holidayRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(updatedHoliday.getTitle());
            existing.setDate(updatedHoliday.getDate());
            existing.setStatus(calculateStatus(updatedHoliday.getDate()));
            return holidayRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }

    private String calculateStatus(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            return "Upcoming Holiday";
        } else {
            return "End Holiday";
        }
    }

}
