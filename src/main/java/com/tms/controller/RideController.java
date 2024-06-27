package com.tms.controller;

import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.ride.CompleteRideResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    @Operation(summary = "Creates a request for new ride using the source and destination x & y coordinates.")
    public ResponseEntity<CreateRideResponse> createRide(@RequestBody CreateRideRequest createRideRequest) {
        CreateRideResponse createRideResponse = rideService.createRide(createRideRequest);
        return new ResponseEntity<>(createRideResponse, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Completes an existing ride.")
    public ResponseEntity<CompleteRideResponse> completeRide(@RequestBody CompleteRideRequest completeRideRequest) {
        CompleteRideResponse completeRideResponse = rideService.completeRide(completeRideRequest);
        return new ResponseEntity<>(completeRideResponse, HttpStatus.ACCEPTED);
    }
}
