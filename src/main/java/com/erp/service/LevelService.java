package com.erp.service;

import java.util.List;

import com.erp.model.Level;

public interface LevelService {

    public Level createLevel(Level level);

    public List<Level> getAllLevels();

    public Level getLevelById(int id);

    public void deleteLevel(int id);

    public Level updateLevel(int id, Level level);
}
