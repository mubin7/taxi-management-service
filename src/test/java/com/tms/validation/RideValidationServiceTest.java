package com.tms.validation;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.TaxiDTO;
import com.tms.exception.CompleteRideException;
import com.tms.exception.NewBookingException;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.persistence.BasePostgresIntegrationTest;
import com.tms.persistence.TaxiRepositoryTestHelper;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.RideService;
import com.tms.service.RideServiceTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class RideValidationServiceTest extends BasePostgresIntegrationTest {

    @Autowired
    private RideValidationService rideValidationService;

    @Autowired
    private RideService rideService;

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @AfterEach
    public void cleanUp() {
        taxiRepository.deleteAll();
        bookingRepository.deleteAll();

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

        assertThrows(NewBookingException.class, () -> rideValidationService.validateCreateRide(createRideRequest));
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

        assertThrows(NewBookingException.class, () -> rideValidationService.validateCreateRide(createRideRequest));
    }

    @Test
    public void whenCompleteRideWithInvalidTaxiStatus_thenThrowsCompleteRideException() {
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
        TaxiDTO taxiDTO = bookingDTO.getTaxiDTO();
        taxiDTO.setTaxiStatus(TaxiStatus.AVAILABLE);
        bookingDTO.setTaxiDTO(taxiDTO);

        CompleteRideRequest completeRideRequest = new CompleteRideRequest(bookingDTO);
        assertThrows(CompleteRideException.class, () -> rideValidationService.validateCompleteRide(completeRideRequest));
    }

    @Test
    public void whenCompleteRideWithInvalidRideStatus_thenThrowsCompleteRideException() {
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
        bookingDTO.setRideStatus(RideStatus.COMPLETED);

        CompleteRideRequest completeRideRequest = new CompleteRideRequest(bookingDTO);
        assertThrows(CompleteRideException.class, () -> rideValidationService.validateCompleteRide(completeRideRequest));
    }
}
