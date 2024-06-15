package com.tms.helper;

import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;

public class TaxiTestHelper {

    public static CreateTaxiRequest createTaxiRequest() {
        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setxPosition(1.0);
        taxiDTO.setyPosition(1.0);
        return new CreateTaxiRequest(taxiDTO);
    }
}
