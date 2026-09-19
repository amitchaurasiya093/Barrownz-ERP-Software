package com.erp.exception;

public class LeaveNotFoundException extends RuntimeException {

    public LeaveNotFoundException(String message) {
        super(message);
    }
}
