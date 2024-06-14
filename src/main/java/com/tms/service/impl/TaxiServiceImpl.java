package com.tms.service.impl;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiRecordFoundException;
import com.tms.mapper.TaxiModelMapper;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiStatusUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiStatusUpdateResponse;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.TaxiService;
import com.tms.validation.TaxiValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        List<TaxiDTO> taxiDTOList = taxiModelMapper.getModelList(taxiList);
        return new TaxiListResponse(taxiDTOList);
    }

    @Override
    public TaxiDTO getTaxi(String taxiId) {
        Taxi taxi = getTaxiEntity(taxiId);
        return taxiModelMapper.getModel(taxi);
    }

    @Override
    public TaxiStatusUpdateResponse updateTaxiStatus(TaxiStatusUpdateRequest taxiStatusUpdateRequest) {
        LOGGER.info("validating update taxi status request");
        taxiValidationService.validateUpdateTaxiStatus(taxiStatusUpdateRequest);
        TaxiDTO taxiDTO = taxiStatusUpdateRequest.taxiDTO();
        Taxi taxi = getTaxiEntity(taxiDTO.getTaxiId());
        taxi.setTaxiStatus(taxiDTO.getTaxiStatus());
        taxi.setxPosition(taxiDTO.getxPosition());
        taxi.setyPosition(taxiDTO.getyPosition());
        LOGGER.info("updating taxi status : " + taxi);
        Taxi persistedTaxi = taxiRepository.save(taxi);
        return new TaxiStatusUpdateResponse(taxiModelMapper.getModel(persistedTaxi));
    }

    private Taxi getTaxiEntity(String taxiDTO) {
        Optional<Taxi> optionalTaxi = taxiRepository.findById(taxiDTO);
        return optionalTaxi.orElseThrow(
                () -> new NoTaxiRecordFoundException("No taxi details found for id : " + taxiDTO));
    }

    @Override
    public CreateTaxiResponse createTaxi(CreateTaxiRequest createTaxiRequest) {
        LOGGER.info("validating create taxi request");
        taxiValidationService.validateCreateTaxi(createTaxiRequest);
        Taxi taxi = taxiModelMapper.getEntity(createTaxiRequest.taxiDTO());
        taxi.setTaxiStatus(TaxiStatus.AVAILABLE);
        LOGGER.info("creating new taxi record");
        Taxi persistedTaxi = taxiRepository.save(taxi);
        return new CreateTaxiResponse(taxiModelMapper.getModel(persistedTaxi));
    }

    @Override
    public TaxiListResponse getTaxisByStatus(TaxiStatus taxiStatus) {
        List<Taxi> taxiList = taxiRepository.findByTaxiStatus(taxiStatus);
        if (taxiList.isEmpty()) {
            LOGGER.error("No taxi record(s) found for the status : {}", taxiStatus);
            throw new NoTaxiRecordFoundException("No taxi record(s) found for the status : " + taxiStatus);
        }
        List<TaxiDTO> taxiDTOList = taxiModelMapper.getModelList(taxiList);
        return new TaxiListResponse(taxiDTOList);
    }
}
