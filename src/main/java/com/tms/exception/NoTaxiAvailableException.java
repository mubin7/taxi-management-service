package com.tms.exception;

public class NoTaxiAvailableException extends RuntimeException {

    public NoTaxiAvailableException(String message) {
        super(message);
    }
}
