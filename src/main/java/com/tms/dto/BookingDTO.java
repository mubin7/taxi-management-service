package com.tms.dto;

import com.tms.constant.RideStatus;

import java.time.LocalDateTime;

public class BookingDTO {
    private String bookingId;
    private TaxiDTO taxiDTO;
    private RideStatus rideStatus;
    private Double srcXPos;
    private Double srcYPos;
    private Double destXPos;
    private Double destYPos;
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

    public Double getSrcXPos() {
        return srcXPos;
    }

    public void setSrcXPos(Double srcXPos) {
        this.srcXPos = srcXPos;
    }

    public Double getSrcYPos() {
        return srcYPos;
    }

    public void setSrcYPos(Double srcYPos) {
        this.srcYPos = srcYPos;
    }

    public Double getDestXPos() {
        return destXPos;
    }

    public void setDestXPos(Double destXPos) {
        this.destXPos = destXPos;
    }

    public Double getDestYPos() {
        return destYPos;
    }

    public void setDestYPos(Double destYPos) {
        this.destYPos = destYPos;
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
