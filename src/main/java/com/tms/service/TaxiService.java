package com.tms.service;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiStatusUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiStatusUpdateResponse;

public interface TaxiService {

    TaxiListResponse getTaxis();

    TaxiDTO getTaxi(String taxiId);

    TaxiStatusUpdateResponse updateTaxiStatus(TaxiStatusUpdateRequest taxiStatusUpdateRequest);

    CreateTaxiResponse createTaxi(CreateTaxiRequest createTaxiRequest);


    TaxiListResponse getTaxisByStatus(TaxiStatus taxiStatus);
}
