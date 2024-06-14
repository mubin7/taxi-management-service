package com.tms.validation.impl;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.TaxiCreationException;
import com.tms.exception.TaxiStatusUpdateException;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiStatusUpdateRequest;
import com.tms.validation.TaxiValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class TaxiValidationServiceImpl implements TaxiValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxiValidationServiceImpl.class);

    @Override
    public void validateUpdateTaxiStatus(TaxiStatusUpdateRequest taxiStatusUpdateRequest) {
        TaxiDTO taxiDTO = taxiStatusUpdateRequest.taxiDTO();
        if (taxiDTO.getTaxiStatus().equals(TaxiStatus.BOOKED)) {
            LOGGER.error("Invalid booking status. (Only AVAILABLE and NOT_AVAILABLE allowed)");
            throw new TaxiStatusUpdateException("Invalid booking status. (Only AVAILABLE and NOT_AVAILABLE allowed)");
        }
    }

    @Override
    public void validateCreateTaxi(CreateTaxiRequest createTaxiRequest) {
        if (ObjectUtils.isEmpty(createTaxiRequest.taxiDTO())) {
            LOGGER.error("Please provide taxi details.");
            throw new TaxiCreationException("Please provide taxi details.");
        }
        if (ObjectUtils.isEmpty(createTaxiRequest.taxiDTO().getxPosition())
                || ObjectUtils.isEmpty(createTaxiRequest.taxiDTO().getyPosition())) {
            LOGGER.error("Please provide taxi coordinates.");
            throw new TaxiCreationException("Please provide taxi coordinates.");
        }
    }
}
