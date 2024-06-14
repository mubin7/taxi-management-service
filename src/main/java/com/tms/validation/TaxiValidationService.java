package com.tms.validation;

import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiStatusUpdateRequest;

public interface TaxiValidationService {

    void validateUpdateTaxiStatus(TaxiStatusUpdateRequest taxiStatusUpdateRequest);

    void validateCreateTaxi(CreateTaxiRequest createTaxiRequest);
}
