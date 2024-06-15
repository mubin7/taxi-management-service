package com.tms.service;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.exception.NewBookingException;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.ride.CompleteRideResponse;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class RideServiceTest extends BasePostgresIntegrationTest {

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
    public void whenCreateRide_thenReturnRide() {
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
        BookingDTO bookingDTO = createRideResponse.bookingDTO();
        assertThat(bookingDTO).isNotNull();
        assertThat(bookingDTO.getBookingId()).isNotNull();
        assertThat(bookingDTO.getRideStatus()).isEqualTo(RideStatus.IN_PROGRESS);
        assertThat(bookingDTO.getRideStartTime()).isNotNull();
        assertThat(bookingDTO.getTaxiDTO().getTaxiStatus()).isEqualTo(TaxiStatus.BOOKED);
    }

    @Test
    public void whenCreateRideWithMissingSource_thenThrowsNewBookingException() {
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(null, null, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
        taxiRepository.saveAll(taxiList);

        assertThrows(NewBookingException.class, () -> rideService.createRide(createRideRequest));
    }

    @Test
    public void whenCreateRideWithMissingDestination_thenThrowsNewBookingException() {
        double srcXPos = 3.0;
        double srcYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, null, null, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
        taxiRepository.saveAll(taxiList);

        assertThrows(NewBookingException.class, () -> rideService.createRide(createRideRequest));
    }

    @Test
    public void whenCreateRideWithExistingBookingId_thenThrowsNewBookingException() {
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
        createRideRequest.bookingDTO().setBookingId(createRideResponse.bookingDTO().getBookingId());

        assertThrows(NewBookingException.class, () -> rideService.createRide(createRideRequest));
    }

    @Test
    public void whenCompleteRide_thenReturnRide() {
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

        CompleteRideRequest completeRideRequest = RideServiceTestHelper.completeRideRequest(createRideResponse.bookingDTO());
        CompleteRideResponse completeRideResponse = rideService.completeRide(completeRideRequest);

        BookingDTO bookingDTO = completeRideResponse.bookingDTO();
        assertThat(bookingDTO).isNotNull();
        assertThat(bookingDTO.getBookingId()).isNotNull();
        assertThat(bookingDTO.getRideStatus()).isEqualTo(RideStatus.COMPLETED);
        assertThat(bookingDTO.getRideEndTime()).isNotNull();
        assertThat(bookingDTO.getTaxiDTO().getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);

    }
}
