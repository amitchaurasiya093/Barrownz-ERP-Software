package com.erp.service;

import java.util.List;

import com.erp.dto.QuoteDto;
import com.erp.model.Quote;

public interface QuoteService {

    public Quote saveQuote(String quoteText, int empCode);

    public List<QuoteDto> getAllQuotes();

    public Quote getLatestQuote();

}
