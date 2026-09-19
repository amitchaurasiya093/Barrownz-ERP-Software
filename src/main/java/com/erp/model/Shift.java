package com.erp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.PreUpdate;

@Entity
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shift_id;

    @Column(nullable = false, length = 100)
    private String shift_name;

    @Column(nullable = false, length = 100)
    private String shift_code;

    @Column(nullable = false)
    private LocalTime login_time;

    @Column(nullable = false)
    private LocalTime logout_time;

    @Column(length = 20)
    private String shift_hours;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OffType saturday_off = OffType.PERMANENT_ON;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OffType sunday_off = OffType.PERMANENT_ON;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OffType holiday_Off = OffType.PERMANENT_ON;

    private LocalTime session1_in;

    private LocalTime session1_out;

    private LocalTime session2_in;

    private LocalTime session2_out;

    private LocalTime session3_in;

    private LocalTime session3_out;

    private LocalTime session4_in;

    private LocalTime session4_out;

    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at = LocalDateTime.now();

    private LocalDateTime updated_at = LocalDateTime.now();

    @PreUpdate
    public void setLastUpdate() {
        this.updated_at = LocalDateTime.now();
    }

    public Shift() {
    }

    public Shift(Integer shift_id, String shift_name, String shift_code, LocalTime login_time, LocalTime logout_time,
            String shift_hours, OffType saturday_off, OffType sunday_off, OffType holiday_Off, LocalTime session1_in,
            LocalTime session1_out, LocalTime session2_in, LocalTime session2_out, LocalTime session3_in,
            LocalTime session3_out, LocalTime session4_in, LocalTime session4_out, LocalDateTime created_at,
            LocalDateTime updated_at) {
        this.shift_id = shift_id;
        this.shift_name = shift_name;
        this.shift_code = shift_code;
        this.login_time = login_time;
        this.logout_time = logout_time;
        this.shift_hours = shift_hours;
        this.saturday_off = saturday_off;
        this.sunday_off = sunday_off;
        this.holiday_Off = holiday_Off;
        this.session1_in = session1_in;
        this.session1_out = session1_out;
        this.session2_in = session2_in;
        this.session2_out = session2_out;
        this.session3_in = session3_in;
        this.session3_out = session3_out;
        this.session4_in = session4_in;
        this.session4_out = session4_out;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getShift_id() {
        return shift_id;
    }

    public void setShift_id(Integer shift_id) {
        this.shift_id = shift_id;
    }

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }

    public String getShift_code() {
        return shift_code;
    }

    public void setShift_code(String shift_code) {
        this.shift_code = shift_code;
    }

    public LocalTime getLogin_time() {
        return login_time;
    }

    public void setLogin_time(LocalTime login_time) {
        this.login_time = login_time;
    }

    public LocalTime getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(LocalTime logout_time) {
        this.logout_time = logout_time;
    }

    public String getShift_hours() {
        return shift_hours;
    }

    public void setShift_hours(String shift_hours) {
        this.shift_hours = shift_hours;
    }

    public OffType getSaturday_off() {
        return saturday_off;
    }

    public void setSaturday_off(OffType saturday_off) {
        this.saturday_off = saturday_off;
    }

    public OffType getSunday_off() {
        return sunday_off;
    }

    public void setSunday_off(OffType sunday_off) {
        this.sunday_off = sunday_off;
    }

    public OffType getHoliday_Off() {
        return holiday_Off;
    }

    public void setHoliday_Off(OffType holiday_Off) {
        this.holiday_Off = holiday_Off;
    }

    public LocalTime getSession1_in() {
        return session1_in;
    }

    public void setSession1_in(LocalTime session1_in) {
        this.session1_in = session1_in;
    }

    public LocalTime getSession1_out() {
        return session1_out;
    }

    public void setSession1_out(LocalTime session1_out) {
        this.session1_out = session1_out;
    }

    public LocalTime getSession2_in() {
        return session2_in;
    }

    public void setSession2_in(LocalTime session2_in) {
        this.session2_in = session2_in;
    }

    public LocalTime getSession2_out() {
        return session2_out;
    }

    public void setSession2_out(LocalTime session2_out) {
        this.session2_out = session2_out;
    }

    public LocalTime getSession3_in() {
        return session3_in;
    }

    public void setSession3_in(LocalTime session3_in) {
        this.session3_in = session3_in;
    }

    public LocalTime getSession3_out() {
        return session3_out;
    }

    public void setSession3_out(LocalTime session3_out) {
        this.session3_out = session3_out;
    }

    public LocalTime getSession4_in() {
        return session4_in;
    }

    public void setSession4_in(LocalTime session4_in) {
        this.session4_in = session4_in;
    }

    public LocalTime getSession4_out() {
        return session4_out;
    }

    public void setSession4_out(LocalTime session4_out) {
        this.session4_out = session4_out;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

}
