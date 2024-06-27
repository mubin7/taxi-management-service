package com.tms.service;

import com.tms.BaseIntegrationTest;
import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.exception.InvalidDateFormatException;
import com.tms.exception.NoBookingRecordFoundException;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class BookingServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RideService rideService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TaxiRepository taxiRepository;

    private CreateRideRequest createRideRequest;

    @AfterEach
    public void cleanUp() {
        bookingRepository.deleteAll();
        taxiRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        createRideRequest = BookingTestHelper.getRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);
    }

    @Test
    public void givenAvailableTaxiWithValidBookingRequest_whenCreateBooking_thenReturnBooking() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        BookingListResponse bookingListResponse = bookingService.getBookings();
        BookingDTO bookingDTO = bookingListResponse.bookingDTOList().getFirst();
        assertThat(bookingListResponse.bookingDTOList()).hasSize(1);
        assertThat(bookingDTO.getBookingId()).isNotNull();
        assertThat(bookingDTO.getRideStatus()).isEqualTo(RideStatus.IN_PROGRESS);
        assertThat(bookingDTO.getRideStartTime()).isNotNull();
        assertThat(bookingDTO.getTaxiDTO().getTaxiStatus()).isEqualTo(TaxiStatus.BOOKED);
    }

    @Test
    public void givenNoBookings_whenGetBookings_thenThrowNoBookingRecordFoundException() {
        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookings());
    }

    @Test
    public void givenInProgressRide_whenSearchInProgressRide_thenReturnInProgressRide() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        BookingListResponse bookingListResponse = bookingService.getBookingsByRideStatus(RideStatus.IN_PROGRESS);
        BookingDTO bookingDTO = bookingListResponse.bookingDTOList().getFirst();
        assertThat(bookingListResponse.bookingDTOList()).hasSize(1);
        assertThat(bookingDTO.getBookingId()).isNotNull();
        assertThat(bookingDTO.getRideStatus()).isEqualTo(RideStatus.IN_PROGRESS);
        assertThat(bookingDTO.getRideStartTime()).isNotNull();
        assertThat(bookingDTO.getTaxiDTO().getTaxiStatus()).isEqualTo(TaxiStatus.BOOKED);
    }

    @Test
    public void givenCompleteRide_whenSearchCompletedRide_thenReturnCompletedRide() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        CreateRideResponse createRideResponse = rideService.createRide(createRideRequest);
        rideService.completeRide(new CompleteRideRequest(createRideResponse.bookingDTO()));

        BookingListResponse bookingListResponse = bookingService.getBookingsByRideStatus(RideStatus.COMPLETED);
        BookingDTO bookingDTO = bookingListResponse.bookingDTOList().getFirst();
        assertThat(bookingListResponse.bookingDTOList()).hasSize(1);
        assertThat(bookingDTO.getBookingId()).isNotNull();
        assertThat(bookingDTO.getRideStatus()).isEqualTo(RideStatus.COMPLETED);
        assertThat(bookingDTO.getRideStartTime()).isNotNull();
        assertThat(bookingDTO.getTaxiDTO().getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
    }

    @Test
    public void givenInProgressRide_whenSearchCompletedRides_thenThrowsNoBookingRecordFoundException() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookingsByRideStatus(RideStatus.COMPLETED));
    }

    @Test
    public void givenInProgressRide_whenSearchBookingsByDate_thenReturnBookingsByDate() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        String date = LocalDate.now().toString();
        BookingListResponse bookingListResponse = bookingService.getBookingsByDate(date);
        BookingDTO bookingDTO = bookingListResponse.bookingDTOList().getFirst();
        assertThat(bookingListResponse.bookingDTOList()).hasSize(1);
        assertThat(bookingDTO.getBookingId()).isNotNull();
        assertThat(bookingDTO.getRideStatus()).isEqualTo(RideStatus.IN_PROGRESS);
        assertThat(bookingDTO.getRideStartTime()).isNotNull();
        assertThat(bookingDTO.getTaxiDTO().getTaxiStatus()).isEqualTo(TaxiStatus.BOOKED);
        assertThat(bookingDTO.getRideStartTime().toLocalDate().toString()).isEqualTo(date);
    }

    @Test
    public void whenCreateBookingAndSearchByDateWithNoBookings_thenThrowsNoBookingRecordFoundException() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        String date = LocalDate.now().plusDays(1).toString();
        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookingsByDate(date));
    }

    @Test
    public void whenCreateBookingAndSearchByInvalidDate_thenThrowsInvalidDateFormatException() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        String date = LocalDateTime.now().toString();
        assertThrows(InvalidDateFormatException.class, () -> bookingService.getBookingsByDate(date));
    }
}
