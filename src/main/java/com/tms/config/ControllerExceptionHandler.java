package com.tms.config;

import com.tms.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoTaxiRecordFoundException.class})
    public ResponseEntity<ErrorMessage> handleNoTaxiRecordFoundException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {TaxiCreationException.class})
    public ResponseEntity<ErrorMessage> handleTaxiCreationException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TaxiStatusUpdateException.class})
    public ResponseEntity<ErrorMessage> handleTaxiStatusUpdateException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NewBookingException.class})
    public ResponseEntity<ErrorMessage> handleNewBookingException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NoTaxiAvailableException.class})
    public ResponseEntity<ErrorMessage> handleNoTaxiAvailableException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NoTaxiAvailableNearbyException.class})
    public ResponseEntity<ErrorMessage> handleNoTaxiAvailableNearbyException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {NoBookingRecordFoundException.class})
    public ResponseEntity<ErrorMessage> handleNoBookingRecordFoundException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CompleteRideException.class})
    public ResponseEntity<ErrorMessage> handleCompleteRideException(RuntimeException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), e.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
