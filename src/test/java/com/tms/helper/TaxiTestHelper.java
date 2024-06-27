package com.tms.helper;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiUpdateResponse;
import com.tms.persistence.entity.Taxi;

import java.util.List;

public class TaxiTestHelper {

    public static Taxi getTaxiWithStatus(TaxiStatus taxiStatus) {
        Taxi taxi = new Taxi();
        taxi.setTaxiStatus(taxiStatus);
        taxi.setCurrXPos(0.0);
        taxi.setCurrYPos(0.0);
        return taxi;
    }

    public static Taxi getTaxiWithStatusAndPosition(TaxiStatus taxiStatus, double xPos, double yPos) {
        Taxi taxi = new Taxi();
        taxi.setTaxiStatus(taxiStatus);
        taxi.setCurrXPos(xPos);
        taxi.setCurrYPos(yPos);
        return taxi;
    }

    public static TaxiDTO getTaxiDTOWithStatusAndPosition(TaxiStatus taxiStatus, double xPos, double yPos) {
        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setTaxiStatus(taxiStatus);
        taxiDTO.setCurrXPos(xPos);
        taxiDTO.setCurrYPos(yPos);
        return taxiDTO;
    }

    public static CreateTaxiRequest getCreateTaxiRequest(TaxiDTO taxiDTO) {
        return new CreateTaxiRequest(taxiDTO);
    }

    public static TaxiUpdateRequest getTaxiUpdateRequest(TaxiDTO taxiDTO) {
        return new TaxiUpdateRequest(taxiDTO);
    }

    public static TaxiListResponse getTaxiListResponse(List<TaxiDTO> taxiDTOS) {
        return new TaxiListResponse(taxiDTOS);
    }

    public static CreateTaxiResponse getCreateTaxiResponse(TaxiDTO taxiDTO) {
        return new CreateTaxiResponse(taxiDTO);
    }

    public static TaxiUpdateResponse getTaxiUpdateResponse(TaxiDTO taxiDTO) {
        return new TaxiUpdateResponse(taxiDTO);
    }

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
