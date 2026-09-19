package com.erp.service;

import java.util.List;

import com.erp.model.Holiday;

public interface HolidayService {

    public Holiday saveHoliday(Holiday holiday);

    public List<Holiday> getAllHolidays();

    public Holiday getHolidayById(Long id);

    public Holiday updateHoliday(Long id, Holiday holiday);

    public void deleteHoliday(Long id);
}
