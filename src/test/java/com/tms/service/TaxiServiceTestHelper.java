package com.tms.service;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;

public class TaxiServiceTestHelper {

    public static CreateTaxiRequest createTaxiRequest(Double xPos, Double yPos) {
        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setCurrXPos(xPos);
        taxiDTO.setCurrYPos(yPos);
        return new CreateTaxiRequest(taxiDTO);
    }

    public static TaxiUpdateRequest taxiUpdateRequest(TaxiDTO taxiDTO, TaxiStatus taxiStatus, Double xPos, Double yPos) {
        taxiDTO.setTaxiStatus(taxiStatus);
        taxiDTO.setCurrXPos(xPos);
        taxiDTO.setCurrYPos(yPos);
        return new TaxiUpdateRequest(taxiDTO);
    }
}
