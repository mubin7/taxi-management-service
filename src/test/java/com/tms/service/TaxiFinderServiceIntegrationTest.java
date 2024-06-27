package com.tms.service;

import com.tms.BaseIntegrationTest;
import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiAvailableException;
import com.tms.exception.NoTaxiAvailableNearbyException;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class TaxiFinderServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaxiFinderService taxiFinderService;

    @Autowired
    private TaxiRepository taxiRepository;
    private CreateRideRequest createRideRequest;

    @AfterEach
    public void cleanUp() {
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
    public void whenCreateRideRequest_thenFindNearestAvailableTaxi() {
        double taxiSrcX = 1.0;
        double taxiSrcY = 1.0;
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, taxiSrcX, taxiSrcY));
        taxiRepository.saveAll(taxiList);

        TaxiDTO nearestTaxi = taxiFinderService.getNearestAvailableTaxi(createRideRequest);
        assertThat(nearestTaxi).isNotNull();
        assertThat(nearestTaxi.getTaxiId()).isNotNull();
        assertThat(nearestTaxi.getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
        assertThat(nearestTaxi.getCurrXPos()).isEqualTo(taxiSrcX);
        assertThat(nearestTaxi.getCurrYPos()).isEqualTo(taxiSrcY);
    }

    @Test
    public void whenCreateRideRequestAndNoTaxiAvailable_thenThrowsNoTaxiAvailableException() {
        List<Taxi> taxiList = Collections.singletonList(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.BOOKED, 1.0, 1.0));
        taxiRepository.saveAll(taxiList);

        assertThrows(NoTaxiAvailableException.class, () -> taxiFinderService.getNearestAvailableTaxi(createRideRequest));
    }

    @Test
    public void whenCreateRideRequestAndTaxiOutOfRange_thenThrowsNoTaxiAvailableNearbyException() {
        Taxi taxi = TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, 10.0, 10.0);
        taxiRepository.save(taxi);

        assertThrows(NoTaxiAvailableNearbyException.class, () -> taxiFinderService.getNearestAvailableTaxi(createRideRequest));
    }

}
