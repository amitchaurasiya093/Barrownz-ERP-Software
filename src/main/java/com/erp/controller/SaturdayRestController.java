package com.erp.controller;

import java.util.List;

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

import com.erp.model.Saturday;
import com.erp.response.ApiResponse;
import com.erp.service.SaturdayService;

@RestController
@RequestMapping("/api/saturday")
@CrossOrigin("*")
public class SaturdayRestController {

    private SaturdayService saturdayService;

    public SaturdayRestController(SaturdayService saturdayService) {
        this.saturdayService = saturdayService;
    }

    // create saturday entry
    @PostMapping
    public ResponseEntity<ApiResponse> createSaturday(@RequestBody Saturday saturday) {
        try {
            if (saturday.getDate() == null || saturday.getMonth() == 0 || saturday.getYear() == 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("date, month, year are required"));
            }
            Saturday saved = saturdayService.saveSaturday(saturday);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("saved", saved));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    // get All saturdays
    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        List<Saturday> list = saturdayService.findAllSaturdays();
        return ResponseEntity.ok(ApiResponse.ok("All", list));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        try {
            saturdayService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(e.getMessage()));
        }
    }

    // update
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse> updateSaturday(@PathVariable Long id, @RequestBody Saturday saturday) {
        try {
            Saturday updated = saturdayService.updateSaturday(id, saturday);
            return ResponseEntity.ok(ApiResponse.ok("updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

}
