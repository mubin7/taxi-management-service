package com.tms.service.impl;

import com.tms.constant.JourneyStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.TaxiDTO;
import com.tms.exception.CompleteRideException;
import com.tms.exception.NewBookingException;
import com.tms.exception.NoBookingRecordFoundException;
import com.tms.exception.NoTaxiRecordFoundException;
import com.tms.mapper.BookingModelMapper;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.ride.CompleteRideResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.RideService;
import com.tms.validation.RideValidationService;
import com.tms.service.TaxiFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RideServiceImpl implements RideService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RideServiceImpl.class);

    private final RideValidationService rideValidationService;
    private final TaxiFinderService taxiFinderService;
    private final BookingRepository bookingRepository;
    private final BookingModelMapper bookingModelMapper;

    private final TaxiRepository taxiRepository;

    public RideServiceImpl(RideValidationService rideValidationService,
                           TaxiFinderService taxiFinderService,
                           BookingRepository bookingRepository,
                           BookingModelMapper bookingModelMapper, TaxiRepository taxiRepository) {
        this.rideValidationService = rideValidationService;
        this.taxiFinderService = taxiFinderService;
        this.bookingRepository = bookingRepository;
        this.bookingModelMapper = bookingModelMapper;
        this.taxiRepository = taxiRepository;
    }


    @Override
    public CreateRideResponse createRide(CreateRideRequest createRideRequest) {
        LOGGER.info("validating new taxi booking request");
        rideValidationService.validateCreateRide(createRideRequest);

        LOGGER.info("getting nearest taxi");
        BookingDTO bookingDTO = createRideRequest.bookingDTO();
        TaxiDTO nearestTaxiDTO = taxiFinderService.getNearestAvailableTaxi(createRideRequest);
        bookingDTO.setTaxiDTO(nearestTaxiDTO);
        LOGGER.info("nearest taxi : {}", nearestTaxiDTO);

        Optional<Taxi> nearestTaxiOptional = taxiRepository.findById(nearestTaxiDTO.getTaxiId());
        Taxi nearestTaxi = nearestTaxiOptional.orElseThrow(
                () -> new NoTaxiRecordFoundException("no taxi record found for id : " + nearestTaxiDTO.getTaxiId())
        );
        Booking booking = getBookingEntity(createRideRequest.bookingDTO(), nearestTaxi);

        try {
            LOGGER.info("booking nearest taxi");
            Booking persistedBooking = bookingRepository.save(booking);
            bookingDTO = bookingModelMapper.getDTO(persistedBooking);
            LOGGER.info("nearest taxi successfully booked with booking details : {}", bookingDTO);
            return new CreateRideResponse(bookingDTO);
        } catch (Exception e) {
            LOGGER.error("create ride request failed for request : {}", createRideRequest);
            throw new NewBookingException("create ride failed for request : " + createRideRequest);
        }
    }

    private Booking getBookingEntity(BookingDTO bookingDTO, Taxi taxi) {
        Booking booking = bookingModelMapper.getEntity(bookingDTO);
        taxi.setTaxiStatus(TaxiStatus.BOOKED);
        booking.setTaxi(taxi);
        booking.getTaxi().setTaxiStatus(TaxiStatus.BOOKED);
        booking.setJourneyStatus(JourneyStatus.IN_PROGRESS);
        booking.setJourneyStartTime(LocalDateTime.now());
        return booking;
    }

    @Override
    public CompleteRideResponse completeRide(CompleteRideRequest completeRideRequest) {
        LOGGER.info("validating complete ride request");
        rideValidationService.validateCompleteRide(completeRideRequest);

        String bookingId = completeRideRequest.bookingDTO().getBookingId();
        LOGGER.info("getting booking details by id {}", bookingId);
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        Booking bookingEntity = optionalBooking.orElseThrow(
                () -> new NoBookingRecordFoundException("no booking record found for booking id : " + bookingId));
        updateBookingDetails(bookingEntity);

        try {
            Booking persistedBooking = bookingRepository.save(bookingEntity);
            BookingDTO bookingDTO = bookingModelMapper.getDTO(persistedBooking);
            return new CompleteRideResponse(bookingDTO);
        } catch (Exception e) {
            LOGGER.error("complete ride failed for request : {}", completeRideRequest);
            throw new CompleteRideException("complete ride failed for request : " + completeRideRequest);
        }
    }

    private void updateBookingDetails(Booking bookingEntity) {
        Taxi taxi = bookingEntity.getTaxi();
        taxi.setTaxiStatus(TaxiStatus.AVAILABLE);
        taxi.setxPosition(bookingEntity.getDestinationXPosition());
        taxi.setyPosition(bookingEntity.getDestinationYPosition());
        bookingEntity.setTaxi(taxi);
        bookingEntity.setJourneyStatus(JourneyStatus.COMPLETED);
        bookingEntity.setJourneyEndTime(LocalDateTime.now());
    }
}
