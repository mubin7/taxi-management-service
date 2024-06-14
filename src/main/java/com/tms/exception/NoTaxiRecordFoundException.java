package com.tms.exception;

public class NoTaxiRecordFoundException extends RuntimeException {

    public NoTaxiRecordFoundException(String message) {
        super(message);
    }
}
