package com.tms.validation;

import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;

public interface RideValidationService {

    void validateCreateRide(CreateRideRequest createRideRequest);

    void validateCompleteRide(CompleteRideRequest completeRideRequest);
}
