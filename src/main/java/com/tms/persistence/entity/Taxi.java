package com.tms.persistence.entity;

import com.tms.constant.TaxiStatus;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Taxi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String taxiId;
    @Enumerated(EnumType.STRING)
    private TaxiStatus taxiStatus;
    private Double xPosition;
    private Double yPosition;

    @OneToMany(mappedBy = "taxi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public Taxi() {
    }

    public Taxi(String taxiId) {
        this.taxiId = taxiId;
    }

    public String getTaxiId() {
        return taxiId;
    }

    public TaxiStatus getTaxiStatus() {
        return taxiStatus;
    }

    public void setTaxiStatus(TaxiStatus taxiStatus) {
        this.taxiStatus = taxiStatus;
    }

    public Double getxPosition() {
        return xPosition;
    }

    public void setxPosition(Double xPosition) {
        this.xPosition = xPosition;
    }

    public Double getyPosition() {
        return yPosition;
    }

    public void setyPosition(Double yPosition) {
        this.yPosition = yPosition;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
