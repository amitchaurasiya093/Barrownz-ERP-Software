package com.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.model.Level;
import com.erp.repository.LevelRepository;

@Service
public class LevelServiceImpl implements LevelService {

    @Autowired
    private LevelRepository levelRepo;

    @Override
    public Level createLevel(Level level) {
        return levelRepo.save(level);
    }

    @Override
    public List<Level> getAllLevels() {
        return levelRepo.findAll();
    }

    @Override
    public Level getLevelById(int id) {
        return levelRepo.findById(id).orElseThrow(() -> new RuntimeException("level id not found: " + id));
    }

    @Override
    public void deleteLevel(int id) {
        levelRepo.deleteById(id);
    }

    @Override
    public Level updateLevel(int id, Level level) {
        Level existingLevel = levelRepo.findById(id).orElseThrow(() -> new RuntimeException("id not found : " + id));

        existingLevel.setLevel(level.getLevel());
        Level updatedLevel = levelRepo.save(existingLevel);
        return updatedLevel;
    }

}
