package com.erp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.dto.QuoteDto;
import com.erp.model.Quote;
import com.erp.service.QuoteService;

@RestController
@RequestMapping("/api/quotes")
@CrossOrigin("*")
public class QuoteRestController {

    @Autowired
    private QuoteService quoteService;

    @PostMapping
    public ResponseEntity<?> addQuote(@RequestBody Map<String, Object> request) {

        String quoteText = (String) request.get("quoteText");
        if (quoteText == null || quoteText.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Quote text is missing.");
        }

        Object empCodeObj = request.get("empCode");
        if (empCodeObj == null) {
            return ResponseEntity.badRequest().body("Employee code is missing.");
        }

        int empCode;
        try {
            empCode = ((Number) empCodeObj).intValue();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Employee code must be a valid number.");
        }

        Quote savedQuote = quoteService.saveQuote(quoteText, empCode);
        QuoteDto dto = new QuoteDto(
                savedQuote.getQuoteId(),
                savedQuote.getQuoteText(),
                savedQuote.getDateCreated(),
                savedQuote.getEmployee().getEmpCode(),
                savedQuote.getEmployee().getFirstName() + " " + savedQuote.getEmployee().getLastName());

        return ResponseEntity.ok(dto);
    }

    // getLatestQuote
    @GetMapping("/latest")
    public ResponseEntity<QuoteDto> getLatestQuote() {
        Quote latestQuote = quoteService.getLatestQuote();

        if (latestQuote == null) {
            return ResponseEntity.noContent().build();
        }

        QuoteDto dto = new QuoteDto(
                latestQuote.getQuoteId(),
                latestQuote.getQuoteText(),
                latestQuote.getDateCreated(),
                latestQuote.getEmployee().getEmpCode(),
                latestQuote.getEmployee().getFirstName() + " " + latestQuote.getEmployee().getLastName());

        return ResponseEntity.ok(dto);
    }

    // get all quotes

    @GetMapping
    public ResponseEntity<List<QuoteDto>> getAllQuotes() {
        return ResponseEntity.ok(quoteService.getAllQuotes());
    }

}
