package com.tms.service.impl;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiRecordFoundException;
import com.tms.mapper.TaxiModelMapper;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiUpdateResponse;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.TaxiService;
import com.tms.validation.TaxiValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaxiServiceImpl implements TaxiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxiServiceImpl.class);

    private final TaxiRepository taxiRepository;
    private final TaxiModelMapper taxiModelMapper;
    private final TaxiValidationService taxiValidationService;

    public TaxiServiceImpl(TaxiRepository taxiRepository, TaxiModelMapper taxiModelMapper, TaxiValidationService taxiValidationService) {
        this.taxiRepository = taxiRepository;
        this.taxiModelMapper = taxiModelMapper;
        this.taxiValidationService = taxiValidationService;
    }

    @Override
    public TaxiListResponse getTaxis() {
        List<Taxi> taxiList = taxiRepository.findAll();
        if (taxiList.isEmpty()) {
            LOGGER.error("No taxi record(s) found.");
            throw new NoTaxiRecordFoundException("No taxi record(s) found.");
        }
        List<TaxiDTO> taxiDTOList = taxiModelMapper.getDTOList(taxiList);
        return new TaxiListResponse(taxiDTOList);
    }

    @Override
    public TaxiDTO getTaxi(String taxiId) {
        Taxi taxi = getTaxiEntity(taxiId);
        return taxiModelMapper.getDTO(taxi);
    }

    @Override
    @Transactional
    public TaxiUpdateResponse updateTaxi(TaxiUpdateRequest taxiUpdateRequest) {
        LOGGER.info("validating update taxi request");
        taxiValidationService.validateUpdateTaxi(taxiUpdateRequest);
        TaxiDTO taxiDTO = taxiUpdateRequest.taxiDTO();
        Taxi taxi = getTaxiEntity(taxiDTO.getTaxiId());
        taxi.setTaxiStatus(taxiDTO.getTaxiStatus());
        taxi.setCurrXPos(taxiDTO.getCurrXPos());
        taxi.setCurrYPos(taxiDTO.getCurrYPos());
        LOGGER.info("updating taxi details : " + taxi);
        Taxi persistedTaxi = taxiRepository.save(taxi);
        return new TaxiUpdateResponse(taxiModelMapper.getDTO(persistedTaxi));
    }

    private Taxi getTaxiEntity(String taxiId) {
        Optional<Taxi> optionalTaxi = taxiRepository.findById(taxiId);
        return optionalTaxi.orElseThrow(
                () -> new NoTaxiRecordFoundException("No taxi details found for id : " + taxiId));
    }

    @Override
    public CreateTaxiResponse createTaxi(CreateTaxiRequest createTaxiRequest) {
        LOGGER.info("validating create taxi request");
        taxiValidationService.validateCreateTaxi(createTaxiRequest);
        Taxi taxi = taxiModelMapper.getEntity(createTaxiRequest.taxiDTO());
        taxi.setTaxiStatus(TaxiStatus.AVAILABLE);
        LOGGER.info("creating new taxi record");
        Taxi persistedTaxi = taxiRepository.save(taxi);
        return new CreateTaxiResponse(taxiModelMapper.getDTO(persistedTaxi));
    }

    @Override
    public TaxiListResponse getTaxiByStatus(TaxiStatus taxiStatus) {
        List<Taxi> taxiList = taxiRepository.findByTaxiStatus(taxiStatus);
        if (taxiList.isEmpty()) {
            LOGGER.error("No taxi record(s) found for the status : {}", taxiStatus);
            throw new NoTaxiRecordFoundException("No taxi record(s) found for the status : " + taxiStatus);
        }
        List<TaxiDTO> taxiDTOList = taxiModelMapper.getDTOList(taxiList);
        return new TaxiListResponse(taxiDTOList);
    }
}
