package com.tms.service;

import com.tms.dto.TaxiDTO;
import com.tms.payload.request.ride.CreateRideRequest;

@FunctionalInterface
public interface TaxiFinderService {

    TaxiDTO getNearestAvailableTaxi(CreateRideRequest createRideRequest);
}
