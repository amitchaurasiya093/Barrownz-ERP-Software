package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Shift;
import com.erp.repository.ShiftRepository;

@Service
public class ShiftServiceImpl implements ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Override
    public Shift createShift(Shift shift) {
        return shiftRepository.save(shift);
    }

    @Override
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    @Override
    public Shift getShiftById(int id) {
        return shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Shift not found with ID: " + id));
    }

    @Override
    public Shift updateShift(int id, Shift shift) {

        Shift existingShift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found with ID: " + id));

        existingShift.setShift_name(shift.getShift_name());
        existingShift.setShift_code(shift.getShift_code());
        existingShift.setLogin_time(shift.getLogin_time());
        existingShift.setLogout_time(shift.getLogout_time());
        existingShift.setShift_hours(shift.getShift_hours());
        existingShift.setSaturday_off(shift.getSaturday_off());
        existingShift.setSunday_off(shift.getSunday_off());
        existingShift.setHoliday_Off(shift.getHoliday_Off());
        existingShift.setSession1_in(shift.getSession1_in());
        existingShift.setSession1_out(shift.getSession1_out());
        existingShift.setSession2_in(shift.getSession2_in());
        existingShift.setSession2_out(shift.getSession2_out());
        existingShift.setSession3_in(shift.getSession3_in());
        existingShift.setSession3_out(shift.getSession3_out());
        existingShift.setSession4_in(shift.getSession4_in());
        existingShift.setSession4_out(shift.getSession4_out());

        Shift updatedShift = shiftRepository.save(existingShift);
        return updatedShift;

    }

    @Override
    public void deleteShift(int id) {
        shiftRepository.deleteById(id);
    }

}
