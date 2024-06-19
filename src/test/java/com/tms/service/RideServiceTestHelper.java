package com.tms.service;

import com.tms.dto.BookingDTO;
import com.tms.dto.RideDTO;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;

public class RideServiceTestHelper {
    public static CreateRideRequest createRideRequest(Double srcXPos, Double srcYPos, Double destXPos, Double destYPos, Double maxDistance) {
        RideDTO rideDTO = new RideDTO(srcXPos, srcYPos, destXPos, destYPos);
        return new CreateRideRequest(rideDTO, maxDistance);
    }

    public static CompleteRideRequest completeRideRequest(BookingDTO bookingDTO) {
        return new CompleteRideRequest(bookingDTO);
    }
}
