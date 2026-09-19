package com.erp.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "saturdays", uniqueConstraints = { @UniqueConstraint(columnNames = { "date" }) })
public class Saturday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "year", nullable = false)
    private int year;

    @JsonProperty("monthDisplay")
    public String getMonthDisplay() {
        return this.month + " (" +
                Month.of(this.month).getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ")";
    }

    public Saturday() {
    }

    public Saturday(Long id, LocalDate date, int month, int year) {
        this.id = id;
        this.date = date;
        this.month = month;
        this.year = year;
    }                        

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
