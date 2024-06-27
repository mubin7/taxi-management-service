package com.tms.service;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NewBookingException;
import com.tms.exception.NoBookingRecordFoundException;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.mapper.BookingModelMapper;
import com.tms.payload.response.ride.CompleteRideResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.impl.RideServiceImpl;
import com.tms.validation.RideValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

public class RideServiceTest {

    private RideService rideService;
    private RideValidationService rideValidationService;
    private TaxiFinderService taxiFinderService;
    private BookingRepository bookingRepository;
    private BookingModelMapper bookingModelMapper;
    private TaxiRepository taxiRepository;

    private Taxi taxi;

    private TaxiDTO taxiDTO;

    private Booking booking;

    private BookingDTO bookingDTO;

    @BeforeEach
    public void setup() {
        rideValidationService = Mockito.mock(RideValidationService.class);
        taxiFinderService = Mockito.mock(TaxiFinderService.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        bookingModelMapper = Mockito.mock(BookingModelMapper.class);
        taxiRepository = Mockito.mock(TaxiRepository.class);
        rideService = new RideServiceImpl(
                rideValidationService, taxiFinderService, bookingRepository, bookingModelMapper, taxiRepository);

        double xPos = 0.0;
        double yPos = 0.0;
        taxi = TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos);
        taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos);
        booking = BookingTestHelper.getBookingWithCoordinates(1.0, 1.0, 3.0, 3.0);
        BookingTestHelper.setNewBookingDetails(booking, LocalDateTime.now(), taxi);
        bookingDTO = BookingTestHelper.getBookingDTO(booking);
    }

    @Test
    public void givenTaxiAvailable_whenCreateRideRequest_thenReturnCreateRideResponse() {
        doNothing().when(rideValidationService).validateCreateRide(any());
        BDDMockito.given(taxiFinderService.getNearestAvailableTaxi(any())).willReturn(taxiDTO);
        BDDMockito.given(taxiRepository.findById(any())).willReturn(Optional.ofNullable(taxi));
        BDDMockito.given(bookingModelMapper.getModel(any())).willReturn(booking);
        BDDMockito.given(bookingRepository.save(any())).willReturn(booking);
        BDDMockito.given(bookingModelMapper.getDTO(any())).willReturn(bookingDTO);

        CreateRideResponse createRideResponse = rideService.createRide(BookingTestHelper
                .getRideRequest(booking.getSrcXPos(), booking.getSrcYPos(), booking.getDestXPos(), booking.getDestYPos(), 5.0));

        assertThat(createRideResponse).isNotNull();
        assertThat(createRideResponse.bookingDTO()).isEqualTo(bookingDTO);
    }

    @Test
    public void givenTaxiAvailable_whenCreateRideRequestWithExceptionDuringBooking_thenThrowNewBookingException() {
        doNothing().when(rideValidationService).validateCreateRide(any());
        BDDMockito.given(taxiFinderService.getNearestAvailableTaxi(any())).willReturn(taxiDTO);
        BDDMockito.given(taxiRepository.findById(any())).willReturn(Optional.ofNullable(taxi));
        BDDMockito.given(bookingModelMapper.getModel(any())).willReturn(booking);
        BDDMockito.given(bookingRepository.save(any())).willThrow(new NewBookingException(""));

        assertThrows(NewBookingException.class, () -> rideService.createRide(BookingTestHelper
                .getRideRequest(booking.getSrcXPos(), booking.getSrcYPos(), booking.getDestXPos(), booking.getDestYPos(), 5.0)));
    }

    @Test
    public void givenRideInProgress_whenCompleteRideRequest_thenReturnCompleteRideResponse() {
        BookingDTO inProgressBookingDTO = BookingTestHelper.getBookingDTO(booking);
        doNothing().when(rideValidationService).validateCompleteRide(any());
        BDDMockito.given(bookingRepository.findById(any())).willReturn(Optional.of(booking));
        BDDMockito.given(bookingRepository.save(any())).willReturn(booking);
        BookingTestHelper.setCompleteBookingDetails(booking);
        BookingDTO completedBookingDTO = BookingTestHelper.getBookingDTO(booking);
        BDDMockito.given(bookingModelMapper.getDTO(any())).willReturn(completedBookingDTO);

        CompleteRideResponse completeRideResponse = rideService.completeRide(BookingTestHelper
                .getCompleteRideRequest(inProgressBookingDTO));

        assertThat(completeRideResponse).isNotNull();
        assertThat(completeRideResponse.bookingDTO()).isNotNull();
        assertThat(completeRideResponse.bookingDTO().getRideStatus()).isEqualTo(RideStatus.COMPLETED);
    }

    @Test
    public void givenNoValidBooking_whenCompleteRideRequest_thenThrowNoBookingRecordFoundException() {
        BookingDTO inProgressBookingDTO = BookingTestHelper.getBookingDTO(booking);
        doNothing().when(rideValidationService).validateCompleteRide(any());
        BDDMockito.given(bookingRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(NoBookingRecordFoundException.class, () -> rideService.completeRide(BookingTestHelper
                .getCompleteRideRequest(inProgressBookingDTO)));
    }
}
