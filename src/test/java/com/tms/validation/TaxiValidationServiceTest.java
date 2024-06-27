package com.tms.validation;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiRecordFoundException;
import com.tms.exception.TaxiCreationException;
import com.tms.exception.TaxiUpdateException;
import com.tms.helper.TaxiTestHelper;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.validation.impl.TaxiValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class TaxiValidationServiceTest {

    private TaxiValidationService taxiValidationService;

    private TaxiRepository taxiRepository;

    @BeforeEach
    public void setup() {
        taxiRepository = Mockito.mock(TaxiRepository.class);
        taxiValidationService = new TaxiValidationServiceImpl(taxiRepository);
    }

    @Test
    public void givenAvailableTaxiRecord_whenTaxiUpdateRequest_thenDoNothing() {
        TaxiDTO taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0);
        given(taxiRepository.findById(any())).willReturn(Optional.of(TaxiTestHelper.getTaxiWithStatus(TaxiStatus.AVAILABLE)));
        assertDoesNotThrow(() -> taxiValidationService.validateUpdateTaxi(TaxiTestHelper.getTaxiUpdateRequest(taxiDTO)));
    }

    @Test
    public void givenTaxiRecord_whenTaxiUpdateRequestToChangeStatusToBooked_thenThrowTaxiUpdateException() {
        TaxiDTO taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.BOOKED, 1.0, 1.0);
        given(taxiRepository.findById(any())).willReturn(Optional.of(TaxiTestHelper.getTaxiWithStatus(TaxiStatus.AVAILABLE)));
        assertThrows(TaxiUpdateException.class, () -> taxiValidationService.validateUpdateTaxi(TaxiTestHelper.getTaxiUpdateRequest(taxiDTO)));
    }

    @Test
    public void givenNoTaxiRecord_whenTaxiUpdateRequest_thenThrowNoTaxiRecordFoundException() {
        TaxiDTO taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.BOOKED, 1.0, 1.0);
        given(taxiRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(NoTaxiRecordFoundException.class, () -> taxiValidationService.validateUpdateTaxi(TaxiTestHelper.getTaxiUpdateRequest(taxiDTO)));
    }

    @Test
    public void givenTaxiRecordWithRideInProgress_whenTaxiUpdateRequestToChangeStatus_thenThrowTaxiUpdateException() {
        TaxiDTO taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.BOOKED, 1.0, 1.0);
        given(taxiRepository.findById(any())).willReturn(Optional.of(TaxiTestHelper.getTaxiWithStatus(TaxiStatus.BOOKED)));
        assertThrows(TaxiUpdateException.class, () -> taxiValidationService.validateUpdateTaxi(TaxiTestHelper.getTaxiUpdateRequest(taxiDTO)));
    }

    @Test
    public void givenValidTaxiData_whenCreateTaxiRequest_thenDoNothing() {
        TaxiDTO taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.AVAILABLE, 1.0, 1.0);
        assertDoesNotThrow(() -> taxiValidationService.validateCreateTaxi(TaxiTestHelper.getCreateTaxiRequest(taxiDTO)));
    }

    @Test
    public void givenEmptyTaxiData_whenCreateTaxiRequest_thenThrowTaxiCreationException() {
        assertThrows(TaxiCreationException.class, () -> taxiValidationService.validateCreateTaxi(TaxiTestHelper.getCreateTaxiRequest(null)));
    }

    @Test
    public void givenNullTaxiCurrentXPosition_whenCreateTaxiRequest_thenThrowTaxiCreationException() {
        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setTaxiStatus(TaxiStatus.AVAILABLE);
        taxiDTO.setCurrYPos(1.0);
        assertThrows(TaxiCreationException.class, () -> taxiValidationService.validateCreateTaxi(TaxiTestHelper.getCreateTaxiRequest(taxiDTO)));
    }

    @Test
    public void givenNullTaxiCurrentYPosition_whenCreateTaxiRequest_thenThrowTaxiCreationException() {
        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setTaxiStatus(TaxiStatus.AVAILABLE);
        taxiDTO.setCurrXPos(1.0);
        assertThrows(TaxiCreationException.class, () -> taxiValidationService.validateCreateTaxi(TaxiTestHelper.getCreateTaxiRequest(taxiDTO)));
    }
}
