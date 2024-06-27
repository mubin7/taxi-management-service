package com.tms.validation;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.TaxiDTO;
import com.tms.exception.CompleteRideException;
import com.tms.exception.NewBookingException;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.validation.impl.RideValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RideValidationServiceTest {

    private RideValidationService rideValidationService;

    private BookingRepository bookingRepository;

    private Booking booking;

    @BeforeEach
    public void setup() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        rideValidationService = new RideValidationServiceImpl(bookingRepository);

        booking = BookingTestHelper.getBookingWithCoordinates(1.0, 1.0, 3.0, 3.0);
        Taxi taxi = TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.BOOKED, 2.0, 2.0);
        BookingTestHelper.setNewBookingDetails(booking, LocalDateTime.now(), taxi);
    }

    @Test
    public void givenValidCreateRideData_whenCreateRideRequest_thenDoNothing() {
        CreateRideRequest createRideRequest = BookingTestHelper.getRideRequest(1.0, 1.0, 3.0, 3.0, 3.0);
        assertDoesNotThrow(() -> rideValidationService.validateCreateRide(createRideRequest));
    }

    @Test
    public void givenMissingSourceCoordinatesInCreateRide_whenCreateRideRequest_thenThrowNewBookingException() {
        CreateRideRequest createRideRequest = BookingTestHelper.getRideRequest(null, null, 3.0, 3.0, 3.0);
        assertThrows(NewBookingException.class, () -> rideValidationService.validateCreateRide(createRideRequest));
    }

    @Test
    public void givenMissingDestinationCoordinatesInCreateRide_whenCreateRideRequest_thenThrowNewBookingException() {
        CreateRideRequest createRideRequest = BookingTestHelper.getRideRequest(1.0, 1.0, null, null, 3.0);
        assertThrows(NewBookingException.class, () -> rideValidationService.validateCreateRide(createRideRequest));
    }

    @Test
    public void givenValidCompleteRideData_whenCompleteRideRequest_thenDoNothing() {
        BookingDTO bookingDTO = BookingTestHelper.getBookingDTO(booking);
        CompleteRideRequest completeRideRequest = BookingTestHelper.getCompleteRideRequest(bookingDTO);
        assertDoesNotThrow(() -> rideValidationService.validateCompleteRide(completeRideRequest));
    }

    @Test
    public void givenCompleteRideDataWithoutBookingId_whenCompleteRideRequest_thenThrowCompleteRideException() {
        BookingDTO bookingDTO = BookingTestHelper.getBookingDTO(booking);
        bookingDTO.setBookingId(null);
        CompleteRideRequest completeRideRequest = BookingTestHelper.getCompleteRideRequest(bookingDTO);
        assertThrows(CompleteRideException.class, () -> rideValidationService.validateCompleteRide(completeRideRequest));
    }

    @Test
    public void givenInvalidTaxiStatus_whenCompleteRideRequest_thenThrowCompleteRideException() {
        BookingDTO bookingDTO = BookingTestHelper.getBookingDTO(booking);
        TaxiDTO taxiDTO = bookingDTO.getTaxiDTO();
        taxiDTO.setTaxiStatus(TaxiStatus.AVAILABLE);
        bookingDTO.setTaxiDTO(taxiDTO);
        CompleteRideRequest completeRideRequest = BookingTestHelper.getCompleteRideRequest(bookingDTO);
        assertThrows(CompleteRideException.class, () -> rideValidationService.validateCompleteRide(completeRideRequest));
    }

    @Test
    public void givenInvalidRideStatus_whenCompleteRideRequest_thenThrowCompleteRideException() {
        BookingDTO bookingDTO = BookingTestHelper.getBookingDTO(booking);
        bookingDTO.setRideStatus(RideStatus.COMPLETED);
        CompleteRideRequest completeRideRequest = BookingTestHelper.getCompleteRideRequest(bookingDTO);
        assertThrows(CompleteRideException.class, () -> rideValidationService.validateCompleteRide(completeRideRequest));
    }
}
