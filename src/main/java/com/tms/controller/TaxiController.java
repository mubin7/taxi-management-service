package com.tms.controller;

import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiStatusUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiStatusUpdateResponse;
import com.tms.service.TaxiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaxiController {

    private final TaxiService taxiService;

    public TaxiController(TaxiService taxiService) {
        this.taxiService = taxiService;
    }

    @GetMapping("/taxis")
    public ResponseEntity<TaxiListResponse> getTaxis() {
        TaxiListResponse taxiListResponse = taxiService.getTaxis();
        return new ResponseEntity<>(taxiListResponse, HttpStatus.OK);
    }

    @GetMapping("/taxis/{id}")
    public ResponseEntity<TaxiDTO> getTaxi(@PathVariable("id") String taxiId) {
        TaxiDTO taxiDTO = taxiService.getTaxi(taxiId);
        return new ResponseEntity<>(taxiDTO, HttpStatus.OK);
    }

    @PostMapping("/taxis")
    public ResponseEntity<?> createTaxi(@RequestBody CreateTaxiRequest createTaxiRequest) {
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        return new ResponseEntity<>(createTaxiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/taxis")
    public ResponseEntity<TaxiStatusUpdateResponse> updateTaxiStatus(@RequestBody TaxiStatusUpdateRequest taxiStatusUpdateRequest) {
        TaxiStatusUpdateResponse taxiStatusUpdateResponse = taxiService.updateTaxiStatus(taxiStatusUpdateRequest);
        return new ResponseEntity<>(taxiStatusUpdateResponse, HttpStatus.OK);
    }

}
