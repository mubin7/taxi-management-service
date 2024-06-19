package com.tms.payload.request.ride;

import com.tms.dto.RideDTO;

public record CreateRideRequest(RideDTO rideDTO, Double maxDistance) {
}
