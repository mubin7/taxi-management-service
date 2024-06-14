package com.tms.exception;

public class NoTaxiAvailableNearbyException extends RuntimeException {
    public NoTaxiAvailableNearbyException(String message) {
        super(message);
    }
}
