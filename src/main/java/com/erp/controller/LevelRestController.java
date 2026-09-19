package com.erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.model.Level;
import com.erp.service.LevelService;

@RestController
@RequestMapping("/api/levels")
@CrossOrigin(origins = "*")
public class LevelRestController {

    @Autowired
    private LevelService levelService;

    // handler for saving level
    @PostMapping
    public ResponseEntity<Level> saveLevel(@RequestBody Level level) {
        Level l = levelService.createLevel(level);
        return new ResponseEntity<>(l, HttpStatus.CREATED);
    }

    // handler for get all levels
    @GetMapping
    public ResponseEntity<List<Level>> showAllLevels() {
        List<Level> allLevels = levelService.getAllLevels();
        return new ResponseEntity<>(allLevels, HttpStatus.OK);
    }

    // handler for single level
    @GetMapping("/{id}")
    public ResponseEntity<Level> getSingleLevel(@PathVariable int id) {
        Level singleLevel = levelService.getLevelById(id);
        return new ResponseEntity<>(singleLevel, HttpStatus.OK);
    }

    // handler for update level
    @PutMapping("/{id}")
    public ResponseEntity<Level> updateLevel(@PathVariable int id, @RequestBody Level level) {
        Level updatedLevel = levelService.updateLevel(id, level);
        return new ResponseEntity<>(updatedLevel, HttpStatus.OK);
    }

    // handler for delete level
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLevel(@PathVariable int id) {
        levelService.deleteLevel(id);
        return new ResponseEntity<>("level deleted successfully", HttpStatus.GONE);
    }
}
