package com.tms.service;

import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.ride.CompleteRideResponse;
import com.tms.payload.response.ride.CreateRideResponse;

public interface RideService {
    CreateRideResponse createRide(CreateRideRequest createRideRequest);

    CompleteRideResponse completeRide(CompleteRideRequest completeRideRequest);
}
