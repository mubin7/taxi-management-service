package com.tms.validation;

import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;

public interface TaxiValidationService {

    void validateUpdateTaxi(TaxiUpdateRequest taxiUpdateRequest);

    void validateCreateTaxi(CreateTaxiRequest createTaxiRequest);
}
