package com.tms.service;

import com.tms.dto.BookingDTO;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;

public class RideServiceTestHelper {
    public static CreateRideRequest createRideRequest(Double srcXPos, Double srcYPos, Double destXPos, Double destYPos, Double maxDistance) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setSrcXPos(srcXPos);
        bookingDTO.setSrcYPos(srcYPos);
        bookingDTO.setDestXPos(destXPos);
        bookingDTO.setDestYPos(destYPos);
        return new CreateRideRequest(bookingDTO, maxDistance);
    }

    public static CompleteRideRequest completeRideRequest(BookingDTO bookingDTO) {
        return new CompleteRideRequest(bookingDTO);
    }
}
