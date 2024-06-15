package com.tms.controller;

import com.tms.constant.RideStatus;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.response.ride.CompleteRideResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.persistence.BasePostgresIntegrationTest;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.RideService;
import com.tms.service.RideServiceTestHelper;
import com.tms.service.TaxiService;
import com.tms.service.TaxiServiceTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RideControllerTest extends BasePostgresIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private TaxiService taxiService;

    @Autowired
    private RideService rideService;

    @AfterEach
    public void cleanUp() {
        bookingRepository.deleteAll();
        taxiRepository.deleteAll();
    }

    @Test
    public void whenCreateRide_thenReturnRide() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(xPos, yPos);
        taxiService.createTaxi(createTaxiRequest);
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(0.0, 0.0, 3.0, 3.0, 5.0);

        ResponseEntity<CreateRideResponse> response = restTemplate
                .postForEntity("http://localhost:" + port + "/rides", createRideRequest, CreateRideResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenCompleteRide_thenReturnCompletedRide() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(xPos, yPos);
        taxiService.createTaxi(createTaxiRequest);
        CreateRideRequest createRideRequest = RideServiceTestHelper
                .createRideRequest(0.0, 0.0, 3.0, 3.0, 5.0);
        CreateRideResponse createRideResponse = rideService.createRide(createRideRequest);

        CompleteRideRequest completeRideRequest = RideServiceTestHelper.completeRideRequest(createRideResponse.bookingDTO());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<CompleteRideRequest> request = new HttpEntity<>(completeRideRequest, headers);

        ResponseEntity<CompleteRideResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/rides",
                HttpMethod.PUT,
                request,
                CompleteRideResponse.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTO().getRideStatus()).isEqualTo(RideStatus.COMPLETED);
    }
}
