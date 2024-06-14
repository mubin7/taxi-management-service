package com.tms.payload.request.ride;

import com.tms.dto.BookingDTO;

public record CompleteRideRequest(BookingDTO bookingDTO) {
}
