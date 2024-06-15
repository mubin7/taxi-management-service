package com.tms.persistence.entity;

import com.tms.constant.TaxiStatus;
import jakarta.persistence.*;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;

@Entity
public class Taxi {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String taxiId;
    @Enumerated(EnumType.STRING)
    private TaxiStatus taxiStatus;
    private Double currXPos;
    private Double currYPos;

    @OneToMany(mappedBy = "taxi", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public Taxi() {
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

    public Double getCurrXPos() {
        return currXPos;
    }

    public void setCurrXPos(Double currXPos) {
        this.currXPos = currXPos;
    }

    public Double getCurrYPos() {
        return currYPos;
    }

    public void setCurrYPos(Double currYPos) {
        this.currYPos = currYPos;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Taxi{" +
                "taxiId='" + taxiId + '\'' +
                ", taxiStatus=" + taxiStatus +
                ", currXPos=" + currXPos +
                ", currYPos=" + currYPos +
                '}';
    }
}