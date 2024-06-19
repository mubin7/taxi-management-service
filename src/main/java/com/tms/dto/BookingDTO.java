package com.tms.dto;

import com.tms.constant.RideStatus;

import java.time.LocalDateTime;

public class BookingDTO {
    private String bookingId;
    private TaxiDTO taxiDTO;
    private RideStatus rideStatus;
    private RideDTO rideDTO;
    private LocalDateTime rideStartTime;
    private LocalDateTime rideEndTime;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public TaxiDTO getTaxiDTO() {
        return taxiDTO;
    }

    public void setTaxiDTO(TaxiDTO taxiDTO) {
        this.taxiDTO = taxiDTO;
    }

    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(RideStatus rideStatus) {
        this.rideStatus = rideStatus;
    }

    public RideDTO getRideDTO() {
        return rideDTO;
    }

    public void setRideDTO(RideDTO rideDTO) {
        this.rideDTO = rideDTO;
    }

    public LocalDateTime getRideStartTime() {
        return rideStartTime;
    }

    public void setRideStartTime(LocalDateTime rideStartTime) {
        this.rideStartTime = rideStartTime;
    }

    public LocalDateTime getRideEndTime() {
        return rideEndTime;
    }

    public void setRideEndTime(LocalDateTime rideEndTime) {
        this.rideEndTime = rideEndTime;
    }
}
