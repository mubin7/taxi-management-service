package com.tms.validation.impl;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.RideDTO;
import com.tms.exception.CompleteRideException;
import com.tms.exception.NewBookingException;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.persistence.repository.BookingRepository;
import com.tms.validation.RideValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RideValidationServiceImpl implements RideValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RideValidationServiceImpl.class);

    private final BookingRepository bookingRepository;

    public RideValidationServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    @Override
    public void validateCreateRide(CreateRideRequest createRideRequest) {
        RideDTO rideDTO = createRideRequest.rideDTO();

        if (rideDTO.srcXPos() == null || rideDTO.srcYPos() == null) {
            LOGGER.error("Source coordinates missing.");
            throw new NewBookingException("Source coordinates missing.");
        }

        if (rideDTO.destXPos() == null || rideDTO.destYPos() == null) {
            LOGGER.error("Destination coordinates missing.");
            throw new NewBookingException("Destination coordinates missing.");
        }
    }

    @Override
    public void validateCompleteRide(CompleteRideRequest completeRideRequest) {
        BookingDTO bookingDTO = completeRideRequest.bookingDTO();

        if (bookingDTO.getBookingId() == null) {
            LOGGER.error("Booking id missing.");
            throw new CompleteRideException("Booking id missing");
        }

        if (bookingDTO.getTaxiDTO().getTaxiStatus().equals(TaxiStatus.AVAILABLE)) {
            LOGGER.error("Invalid taxi status.");
            throw new CompleteRideException("Invalid taxi status");
        }

        if (bookingDTO.getRideStatus().equals(RideStatus.COMPLETED)) {
            LOGGER.error("Invalid ride status.");
            throw new CompleteRideException("Invalid ride status");
        }
    }
}
