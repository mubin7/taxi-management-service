package com.tms.service;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.exception.InvalidDateFormatException;
import com.tms.exception.NoBookingRecordFoundException;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.persistence.BasePostgresIntegrationTest;
import com.tms.persistence.TaxiRepositoryTestHelper;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class BookingServiceTest extends BasePostgresIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RideService rideService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TaxiRepository taxiRepository;

    @AfterEach
    public void cleanUp() {
        bookingRepository.deleteAll();
        taxiRepository.deleteAll();
    }

    @Test
    public void whenCreateBooking_thenReturnBooking() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
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
    public void whenNotCreatedBooking_thenThrowsNoBookingRecordFoundException() {
        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookings());
    }

    @Test
    public void whenCreateBookingAndSearchInProgressRide_thenReturnInProgressRide() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
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
    public void whenCreateBookingAndCompleteRideAndSearchCompletedRide_thenReturnCompletedRide() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
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
    public void whenCreateBookingAndSearchCompletedRides_thenThrowsNoBookingRecordFoundException() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookingsByRideStatus(RideStatus.COMPLETED));
    }

    @Test
    public void whenCreateBookingAndSearchByDate_thenReturnBookingsByDate() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
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
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        String date = LocalDate.now().plusDays(1).toString();
        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookingsByDate(date));
    }

    @Test
    public void whenCreateBookingAndSearchByInvalidDate_thenThrowsInvalidDateFormatException() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
        taxiRepository.saveAll(taxiList);

        rideService.createRide(createRideRequest);

        String date = LocalDateTime.now().toString();
        assertThrows(InvalidDateFormatException.class, () -> bookingService.getBookingsByDate(date));
    }
}
