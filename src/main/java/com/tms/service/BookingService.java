package com.tms.service;

import com.tms.constant.JourneyStatus;
import com.tms.payload.response.booking.BookingListResponse;

public interface BookingService {
    BookingListResponse getBookings();

    BookingListResponse getBookingsByJourneyStatus(JourneyStatus journeyStatus);

    BookingListResponse getBookingsByDate(String date);
}
