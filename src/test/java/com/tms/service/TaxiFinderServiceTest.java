package com.tms.service;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiAvailableException;
import com.tms.exception.NoTaxiAvailableNearbyException;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.mapper.TaxiModelMapper;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.impl.TaxiFinderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class TaxiFinderServiceTest {

    private TaxiFinderService taxiFinderService;

    private TaxiRepository taxiRepository;

    private TaxiModelMapper taxiModelMapper;

    private Taxi taxi;

    private TaxiDTO taxiDTO;

    private CreateRideRequest createRideRequest;

    @BeforeEach
    public void setup() {
        taxiRepository = Mockito.mock(TaxiRepository.class);
        taxiModelMapper = Mockito.mock(TaxiModelMapper.class);
        taxiFinderService = new TaxiFinderServiceImpl(taxiRepository, taxiModelMapper);

        double xPos = 0.0;
        double yPos = 0.0;
        taxi = TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos);
        taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos);

        createRideRequest = BookingTestHelper.getRideRequest(1.0, 1.0, 3.0, 3.0, 3.0);
    }

    @Test
    public void givenTaxiAvailable_whenSearchForNearestAvailableTaxi_thenReturnNearestAvailableTaxi() {
        BDDMockito.given(taxiRepository.findByTaxiStatus(any())).willReturn(Collections.singletonList(taxi));
        BDDMockito.given(taxiModelMapper.getDTOList(any())).willReturn(Collections.singletonList(taxiDTO));
        TaxiDTO nearestAvailableTaxiDTO = taxiFinderService.getNearestAvailableTaxi(
                createRideRequest);

        assertThat(nearestAvailableTaxiDTO).isNotNull();
        assertThat(nearestAvailableTaxiDTO.getTaxiStatus()).isEqualTo(taxi.getTaxiStatus());
    }

    @Test
    public void givenTaxiNotAvailable_whenSearchForNearestAvailableTaxi_thenReturnThrowNoTaxiAvailableException() {
        BDDMockito.given(taxiRepository.findByTaxiStatus(any())).willReturn(Collections.emptyList());
        BDDMockito.given(taxiModelMapper.getDTOList(any())).willReturn(Collections.singletonList(taxiDTO));

        assertThrows(NoTaxiAvailableException.class, () -> taxiFinderService.getNearestAvailableTaxi(createRideRequest));
    }

    @Test
    public void givenTaxiAvailableOutOfRange_whenSearchForNearestAvailableTaxi_thenReturnThrowNoTaxiAvailableNearbyException() {
        double taxiSrcX = 5.0;
        double taxiSrcY = 5.0;
        BDDMockito.given(taxiRepository.findByTaxiStatus(any()))
                .willReturn(Collections.singletonList(TaxiTestHelper
                        .getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, taxiSrcX, taxiSrcY)));
        BDDMockito.given(taxiModelMapper.getDTOList(any()))
                .willReturn(Collections.singletonList(TaxiTestHelper
                        .getTaxiDTOWithStatusAndPosition(TaxiStatus.AVAILABLE, taxiSrcX, taxiSrcY)));

        assertThrows(NoTaxiAvailableNearbyException.class, () -> taxiFinderService.getNearestAvailableTaxi(createRideRequest));
    }
}
