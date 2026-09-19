package com.erp.service;

import java.util.List;

import com.erp.model.Saturday;

public interface SaturdayService {

    public Saturday saveSaturday(Saturday saturday);

    public List<Saturday> findAllSaturdays();

    public void deleteById(Long id);

    public Saturday updateSaturday(Long id, Saturday saturday);

}
