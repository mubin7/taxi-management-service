package com.tms.controller;

import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiUpdateResponse;
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
    public ResponseEntity<CreateTaxiResponse> createTaxi(@RequestBody CreateTaxiRequest createTaxiRequest) {
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        return new ResponseEntity<>(createTaxiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/taxis")
    public ResponseEntity<TaxiUpdateResponse> updateTaxi(@RequestBody TaxiUpdateRequest taxiUpdateRequest) {
        TaxiUpdateResponse taxiUpdateResponse = taxiService.updateTaxi(taxiUpdateRequest);
        return new ResponseEntity<>(taxiUpdateResponse, HttpStatus.OK);
    }

}
