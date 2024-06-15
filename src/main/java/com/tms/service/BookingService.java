package com.tms.service;

import com.tms.constant.RideStatus;
import com.tms.payload.response.booking.BookingListResponse;

public interface BookingService {
    BookingListResponse getBookings();

    BookingListResponse getBookingsByRideStatus(RideStatus rideStatus);

    BookingListResponse getBookingsByDate(String date);
}
