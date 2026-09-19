package com.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.erp.model.Shift;
import com.erp.service.ShiftService;

@RestController
@RequestMapping("/api/shifts")
@CrossOrigin(origins = "*")
public class ShiftRestController {

    @Autowired
    private ShiftService shiftService;

    // create shift handler
    @PostMapping
    public ResponseEntity<Shift> createShift(@RequestBody Shift shift) {
        Shift savedShift = shiftService.createShift(shift);
        return new ResponseEntity<>(savedShift, HttpStatus.CREATED);
    }

    // get all shifts handler

    @GetMapping
    public ResponseEntity<List<Shift>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShifts();
        return new ResponseEntity<>(shifts, HttpStatus.OK);
    }

    // get shift by id handler

    @GetMapping("/{id}")
    public ResponseEntity<Shift> getShiftById(@PathVariable int id) {
        Shift shift = shiftService.getShiftById(id);
        return new ResponseEntity<>(shift, HttpStatus.OK);
    }

    // update shift handler

    @PutMapping("/{id}")
    public ResponseEntity<Shift> updateShift(@PathVariable int id, @RequestBody Shift shift) {
        Shift updatedShift = shiftService.updateShift(id, shift);
        return new ResponseEntity<>(updatedShift, HttpStatus.OK);
    }

    // delete shift handler

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShift(@PathVariable int id) {
        // shiftService.deleteShift(id);
        // return new ResponseEntity<>("Shift deleted successfully", HttpStatus.GONE);

        // new code
        try {
            shiftService.deleteShift(id);
            return ResponseEntity.ok("Shift deleted successfully.");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Shift is assigned to an employee and cannot be deleted.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the shift");
        }
    }
}
