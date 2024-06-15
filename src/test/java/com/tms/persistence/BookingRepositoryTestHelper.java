package com.tms.persistence;

import com.tms.constant.RideStatus;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;

import java.time.LocalDateTime;

public class BookingRepositoryTestHelper {

    public static Booking createBooking(Taxi taxi) {
        Booking booking = new Booking();
        booking.setRideStatus(RideStatus.IN_PROGRESS);
        booking.setTaxi(taxi);
        booking.setSrcXPos(0.0);
        booking.setSrcYPos(0.0);
        booking.setDestXPos(3.0);
        booking.setDestYPos(3.0);
        booking.setRideStartTime(LocalDateTime.now());
        return booking;
    }
}
