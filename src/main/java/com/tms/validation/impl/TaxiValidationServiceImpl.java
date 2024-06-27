package com.tms.validation.impl;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiRecordFoundException;
import com.tms.exception.TaxiCreationException;
import com.tms.exception.TaxiUpdateException;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.validation.TaxiValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaxiValidationServiceImpl implements TaxiValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxiValidationServiceImpl.class);

    private final TaxiRepository taxiRepository;

    public TaxiValidationServiceImpl(TaxiRepository taxiRepository) {
        this.taxiRepository = taxiRepository;
    }

    @Override
    public void validateUpdateTaxi(TaxiUpdateRequest taxiUpdateRequest) {
        TaxiDTO taxiDTO = taxiUpdateRequest.taxiDTO();
        Optional<Taxi> optionalTaxi = taxiRepository.findById(taxiDTO.getTaxiId());
        Taxi taxi = optionalTaxi.orElseThrow(() -> new NoTaxiRecordFoundException("No taxi details found for id : " + taxiDTO.getTaxiId()));

        if (taxi.getTaxiStatus().equals(TaxiStatus.BOOKED)) {
            LOGGER.error("Ride in progress. Please complete the ride to update the taxi status.");
            throw new TaxiUpdateException("Ride in progress. Please complete the ride to update the taxi status.");
        }

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
