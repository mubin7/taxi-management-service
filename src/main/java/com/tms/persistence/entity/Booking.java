package com.tms.persistence.entity;

import com.tms.constant.RideStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bookingId;
    @ManyToOne
    @JoinColumn(name = "taxi_id")
    private Taxi taxi;
    @Enumerated(EnumType.STRING)
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

    public Taxi getTaxi() {
        return taxi;
    }

    public void setTaxi(Taxi taxi) {
        this.taxi = taxi;
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

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", taxi=" + taxi +
                ", journeyStatus=" + rideStatus +
                ", srcXPos=" + srcXPos +
                ", srcYPos=" + srcYPos +
                ", destXPos=" + destXPos +
                ", destYPos=" + destYPos +
                ", rideStartTime=" + rideStartTime +
                ", rideEndTime=" + rideEndTime +
                '}';
    }
}
