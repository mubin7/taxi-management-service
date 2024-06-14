package com.tms.dto;

import com.tms.constant.JourneyStatus;

import java.time.LocalDateTime;

public class BookingDTO {
    private String bookingId;
    private TaxiDTO taxiDTO;
    private JourneyStatus journeyStatus;
    private Double sourceXPosition;
    private Double sourceYPosition;
    private Double destinationXPosition;
    private Double destinationYPosition;
    private LocalDateTime journeyStartTime;
    private LocalDateTime journeyEndTime;

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

    public JourneyStatus getJourneyStatus() {
        return journeyStatus;
    }

    public void setJourneyStatus(JourneyStatus journeyStatus) {
        this.journeyStatus = journeyStatus;
    }

    public Double getSourceXPosition() {
        return sourceXPosition;
    }

    public void setSourceXPosition(Double sourceXPosition) {
        this.sourceXPosition = sourceXPosition;
    }

    public Double getSourceYPosition() {
        return sourceYPosition;
    }

    public void setSourceYPosition(Double sourceYPosition) {
        this.sourceYPosition = sourceYPosition;
    }

    public Double getDestinationXPosition() {
        return destinationXPosition;
    }

    public void setDestinationXPosition(Double destinationXPosition) {
        this.destinationXPosition = destinationXPosition;
    }

    public Double getDestinationYPosition() {
        return destinationYPosition;
    }

    public void setDestinationYPosition(Double destinationYPosition) {
        this.destinationYPosition = destinationYPosition;
    }

    public LocalDateTime getJourneyStartTime() {
        return journeyStartTime;
    }

    public void setJourneyStartTime(LocalDateTime journeyStartTime) {
        this.journeyStartTime = journeyStartTime;
    }

    public LocalDateTime getJourneyEndTime() {
        return journeyEndTime;
    }

    public void setJourneyEndTime(LocalDateTime journeyEndTime) {
        this.journeyEndTime = journeyEndTime;
    }
}
