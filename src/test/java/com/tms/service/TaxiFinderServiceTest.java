package com.tms.service;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiAvailableException;
import com.tms.exception.NoTaxiAvailableNearbyException;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.persistence.BasePostgresIntegrationTest;
import com.tms.persistence.TaxiRepositoryTestHelper;
import com.tms.persistence.entity.Taxi;
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
public class TaxiFinderServiceTest extends BasePostgresIntegrationTest {

    @Autowired
    private TaxiFinderService taxiFinderService;

    @Autowired
    private TaxiRepository taxiRepository;

    @AfterEach
    public void cleanUp() {
        taxiRepository.deleteAll();
    }

    @Test
    public void whenCreateRideRequest_thenFindNearestAvailableTaxi() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.AVAILABLE);
        taxiRepository.saveAll(taxiList);

        TaxiDTO nearestTaxi = taxiFinderService.getNearestAvailableTaxi(createRideRequest);
        assertThat(nearestTaxi).isNotNull();
        assertThat(nearestTaxi.getTaxiId()).isNotNull();
        assertThat(nearestTaxi.getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
        assertThat(nearestTaxi.getCurrXPos()).isEqualTo(srcXPos);
        assertThat(nearestTaxi.getCurrYPos()).isEqualTo(srcYPos);
    }

    @Test
    public void whenCreateRideRequestAndNoTaxiAvailable_thenThrowsNoTaxiAvailableException() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(2, TaxiStatus.BOOKED);
        taxiRepository.saveAll(taxiList);

        assertThrows(NoTaxiAvailableException.class, () -> taxiFinderService.getNearestAvailableTaxi(createRideRequest));
    }

    @Test
    public void whenCreateRideRequestAndTaxiOutOfRange_thenThrowsNoTaxiAvailableNearbyException() {
        double srcXPos = 0.0;
        double srcYPos = 0.0;
        double destXPos = 3.0;
        double destYPos = 3.0;
        double maxDistance = 5.0;
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(srcXPos, srcYPos, destXPos, destYPos, maxDistance);

        Taxi taxi = TaxiRepositoryTestHelper.createTaxi(10.0, 10.0);
        taxiRepository.save(taxi);

        assertThrows(NoTaxiAvailableNearbyException.class, () -> taxiFinderService.getNearestAvailableTaxi(createRideRequest));
    }

}
