package com.tms.controller;

import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.ride.CompleteRideResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.service.RideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/rides")
    public ResponseEntity<CreateRideResponse> createRide(@RequestBody CreateRideRequest createRideRequest) {
        CreateRideResponse createRideResponse = rideService.createRide(createRideRequest);
        return new ResponseEntity<>(createRideResponse, HttpStatus.CREATED);
    }

    @PutMapping("/rides")
    public ResponseEntity<CompleteRideResponse> completeRide(@RequestBody CompleteRideRequest completeRideRequest) {
        CompleteRideResponse completeRideResponse = rideService.completeRide(completeRideRequest);
        return new ResponseEntity<>(completeRideResponse, HttpStatus.ACCEPTED);
    }
}
