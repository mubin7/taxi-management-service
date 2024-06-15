package com.tms.service;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiUpdateResponse;

public interface TaxiService {

    TaxiListResponse getTaxis();

    TaxiDTO getTaxi(String taxiId);

    TaxiUpdateResponse updateTaxi(TaxiUpdateRequest taxiUpdateRequest);

    CreateTaxiResponse createTaxi(CreateTaxiRequest createTaxiRequest);

    TaxiListResponse getTaxiByStatus(TaxiStatus taxiStatus);
}
