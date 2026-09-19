package com.erp.dto;

import java.time.LocalDateTime;

public class QuoteDto {

    private int quoteId;
    private String quoteText;
    private LocalDateTime dateCreated;
    private int empCode;
    private String employeeName;

    public QuoteDto() {
    }

    public QuoteDto(int quoteId, String quoteText, LocalDateTime dateCreated, int empCode, String employeeName) {
        this.quoteId = quoteId;
        this.quoteText = quoteText;
        this.dateCreated = dateCreated;
        this.empCode = empCode;
        this.employeeName = employeeName;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getEmpCode() {
        return empCode;
    }

    public void setEmpCode(int empCode) {
        this.empCode = empCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

}
