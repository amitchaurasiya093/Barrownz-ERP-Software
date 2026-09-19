package com.erp.controller;

import com.erp.model.Holiday;
import com.erp.repository.HolidayRepository;
import com.erp.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@CrossOrigin(origins = "*")
public class HolidayRestController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepo;

    @GetMapping
    public List<Holiday> getAllHoliday() {
        return holidayRepo.findAll();
    }

    @PostMapping
    public Holiday createHoliday(@RequestBody Holiday holiday) {
        return holidayService.saveHoliday(holiday);
    }

    @GetMapping("/all")
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @GetMapping("/{id}")
    public Holiday getHolidayById(@PathVariable Long id) {
        return holidayService.getHolidayById(id);
    }

    @PutMapping("/{id}")
    public Holiday updateHoliday(@PathVariable Long id, @RequestBody Holiday holiday) {
        return holidayService.updateHoliday(id, holiday);
    }

    @DeleteMapping("/{id}")
    public void deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
    }
}
