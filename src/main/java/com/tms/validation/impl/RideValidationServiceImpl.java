package com.tms.validation.impl;

import com.tms.constant.JourneyStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.exception.CompleteRideException;
import com.tms.exception.NewBookingException;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.repository.BookingRepository;
import com.tms.validation.RideValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class RideValidationServiceImpl implements RideValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RideValidationServiceImpl.class);

    private final BookingRepository bookingRepository;

    public RideValidationServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    @Override
    public void validateCreateRide(CreateRideRequest createRideRequest) {
        BookingDTO bookingDTO = createRideRequest.bookingDTO();

        if (!ObjectUtils.isEmpty(bookingDTO.getBookingId())) {
            Optional<Booking> bookingOptional = bookingRepository.findById(bookingDTO.getBookingId());
            bookingOptional.orElseThrow(() -> new NewBookingException("Booking already exists."));
        }

        if (ObjectUtils.isEmpty(bookingDTO.getSourceXPosition())
                || ObjectUtils.isEmpty(bookingDTO.getSourceYPosition())) {
            LOGGER.error("Source coordinates missing.");
            throw new NewBookingException("Source coordinates missing.");
        }

        if (ObjectUtils.isEmpty(bookingDTO.getDestinationXPosition())
                || ObjectUtils.isEmpty(bookingDTO.getDestinationYPosition())) {
            LOGGER.error("Destination coordinates missing.");
            throw new NewBookingException("Destination coordinates missing.");
        }
    }

    @Override
    public void validateCompleteRide(CompleteRideRequest completeRideRequest) {
        BookingDTO bookingDTO = completeRideRequest.bookingDTO();

        if (bookingDTO.getBookingId().isEmpty()) {
            LOGGER.error("Booking id missing.");
            throw new CompleteRideException("Booking id missing");
        }

        if (bookingDTO.getTaxiDTO().getTaxiStatus().equals(TaxiStatus.AVAILABLE)) {
            LOGGER.error("Invalid taxi status.");
            throw new CompleteRideException("Invalid taxi status");
        }

        if (bookingDTO.getJourneyStatus().equals(JourneyStatus.COMPLETED)) {
            LOGGER.error("Invalid journey status.");
            throw new CompleteRideException("Invalid journey status");
        }
    }
}
