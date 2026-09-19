package com.erp.service;

import java.util.List;

import com.erp.model.Shift;

public interface ShiftService {

    public Shift createShift(Shift shift);

    public List<Shift> getAllShifts();

    public Shift getShiftById(int id);

    public Shift updateShift(int id, Shift shift);

    public void deleteShift(int id);

}
