package com.tms.exception;

public class NoBookingRecordFoundException extends RuntimeException {

    public NoBookingRecordFoundException(String message) {
        super(message);
    }
}
