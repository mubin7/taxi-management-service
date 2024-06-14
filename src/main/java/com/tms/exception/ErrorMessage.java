package com.tms.exception;

import java.time.LocalDateTime;

public class ErrorMessage {

    private final int statusCode;
    private final LocalDateTime time;
    private final String message;

    public ErrorMessage(final int statusCode, final LocalDateTime time, final String message) {
        this.statusCode = statusCode;
        this.time = time;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
