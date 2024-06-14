package com.tms.payload.request.ride;

import com.tms.dto.BookingDTO;

public record CreateRideRequest(BookingDTO bookingDTO, Double maxDistance) {
}
