package com.tms.service.impl;

import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiAvailableException;
import com.tms.exception.NoTaxiAvailableNearbyException;
import com.tms.mapper.TaxiModelMapper;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.TaxiFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TaxiFinderServiceImpl implements TaxiFinderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxiFinderServiceImpl.class);

    private final TaxiRepository taxiRepository;

    private final TaxiModelMapper taxiModelMapper;

    public TaxiFinderServiceImpl(TaxiRepository taxiRepository, TaxiModelMapper taxiModelMapper) {
        this.taxiRepository = taxiRepository;
        this.taxiModelMapper = taxiModelMapper;
    }

    @Override
    public TaxiDTO getNearestAvailableTaxi(CreateRideRequest createRideRequest) {
        BookingDTO bookingDTO = createRideRequest.bookingDTO();
        return getAvailableTaxis().stream()
                .min(Comparator.comparingDouble(taxi -> calculateDistance(bookingDTO, taxi)))
                .filter(taxi -> createRideRequest.maxDistance() > calculateDistance(bookingDTO, taxi))
                .orElseThrow(() -> new NoTaxiAvailableNearbyException("Currently no taxi available nearby."));
    }

    private List<TaxiDTO> getAvailableTaxis() {
        List<Taxi> availableTaxis = taxiRepository.findByTaxiStatus(TaxiStatus.AVAILABLE);
        if (availableTaxis.isEmpty()) {
            LOGGER.error("No taxi currently available.");
            throw new NoTaxiAvailableException("No taxi currently available.");
        }
        return taxiModelMapper.getModelList(availableTaxis);
    }

    private Double calculateDistance(BookingDTO bookingDTO, TaxiDTO taxiDTO) {
        double xCordDist = taxiDTO.getxPosition() - bookingDTO.getSourceXPosition();
        double yCordDist = taxiDTO.getyPosition() - bookingDTO.getSourceYPosition();
        double xCordDistSquare = xCordDist * xCordDist;
        double yCordDistSquare = yCordDist * yCordDist;
        return Math.sqrt(xCordDistSquare + yCordDistSquare);
    }
}
