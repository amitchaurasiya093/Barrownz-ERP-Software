package com.erp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.dto.QuoteDto;
import com.erp.model.Employee;
import com.erp.model.Quote;
import com.erp.repository.EmployeeRepository;
import com.erp.repository.QuoteRepository;

@Service
public class QuoteServiceImpl implements QuoteService {

    @Autowired
    private QuoteRepository quoteRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Override
    public Quote saveQuote(String quoteText, int empCode) {
        System.out.println("Received empCode: " + empCode);
        Employee employee = employeeRepo.findByEmpCode(empCode);

        if (employee == null) {
            throw new RuntimeException("Employee not found with empCode: " + empCode);
        }

        Quote quote = new Quote();
        quote.setQuoteText(quoteText);
        quote.setEmployee(employee);

        Quote savedQuote = quoteRepo.save(quote);
        return savedQuote;
    }

    @Override
    public List<QuoteDto> getAllQuotes() {
        List<Quote> quotes = quoteRepo.findAllByOrderByDateCreatedDesc();
        return quotes.stream().map(q -> new QuoteDto(
                q.getQuoteId(),
                q.getQuoteText(),
                q.getDateCreated(),
                q.getEmployee().getEmpCode(),
                q.getEmployee().getFirstName() + " " + q.getEmployee().getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public Quote getLatestQuote() {

        return quoteRepo.findTopByOrderByDateCreatedDesc();

    }

}
