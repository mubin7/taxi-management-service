package com.tms.validation.impl;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.TaxiCreationException;
import com.tms.exception.TaxiUpdateException;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.validation.TaxiValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaxiValidationServiceImpl implements TaxiValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxiValidationServiceImpl.class);

    @Override
    public void validateUpdateTaxi(TaxiUpdateRequest taxiUpdateRequest) {
        TaxiDTO taxiDTO = taxiUpdateRequest.taxiDTO();
        if (taxiDTO.getTaxiStatus().equals(TaxiStatus.BOOKED)) {
            LOGGER.error("Invalid booking status. (Only AVAILABLE and NOT_AVAILABLE allowed)");
            throw new TaxiUpdateException("Invalid booking status. (Only AVAILABLE and NOT_AVAILABLE allowed)");
        }
    }

    @Override
    public void validateCreateTaxi(CreateTaxiRequest createTaxiRequest) {
        if (createTaxiRequest.taxiDTO() == null) {
            LOGGER.error("Please provide taxi details.");
            throw new TaxiCreationException("Please provide taxi details.");
        }
        if (createTaxiRequest.taxiDTO().getCurrXPos() == null || createTaxiRequest.taxiDTO().getCurrYPos() == null) {
            LOGGER.error("Please provide taxi coordinates.");
            throw new TaxiCreationException("Please provide taxi coordinates.");
        }
    }
}
