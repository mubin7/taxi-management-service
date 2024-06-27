package com.tms.controller;

import com.tms.BaseIntegrationTest;
import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.RideService;
import com.tms.service.TaxiService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DashboardControllerIntegrationTest extends BaseIntegrationTest {

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
    public void givenInProgressBooking_whenGetBookings_thenReturnAllBookings() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPos, yPos);
        taxiService.createTaxi(createTaxiRequest);

        CreateRideRequest createRideRequest = BookingTestHelper.getRideRequest(0.0, 0.0, 3.0, 3.0, 5.0);
        rideService.createRide(createRideRequest);

        ResponseEntity<BookingListResponse> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/dashboard/bookings", BookingListResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList()).hasSize(1);
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList().getFirst().getBookingId()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList().getFirst().getRideStatus()).isEqualTo(RideStatus.IN_PROGRESS);
    }

    @Test
    public void givenInProgressBooking_whenGetBookingsByStatus_thenReturnAllBookingsByStatus() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPos, yPos);
        taxiService.createTaxi(createTaxiRequest);
        CreateRideRequest createRideRequest = BookingTestHelper.getRideRequest(0.0, 0.0, 3.0, 3.0, 5.0);
        rideService.createRide(createRideRequest);

        ResponseEntity<BookingListResponse> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/dashboard/bookings/status?status=" + RideStatus.IN_PROGRESS, BookingListResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList()).hasSize(1);
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList().getFirst().getBookingId()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList().getFirst().getRideStatus()).isEqualTo(RideStatus.IN_PROGRESS);
    }

    @Test
    public void givenInProgressBooking_whenGetBookingsByDate_thenReturnAllBookingsByDate() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPos, yPos);
        taxiService.createTaxi(createTaxiRequest);
        CreateRideRequest createRideRequest = BookingTestHelper.getRideRequest(0.0, 0.0, 3.0, 3.0, 5.0);
        rideService.createRide(createRideRequest);

        ResponseEntity<BookingListResponse> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/dashboard/bookings/date?date=" + LocalDate.now(), BookingListResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList()).hasSize(1);
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList().getFirst().getBookingId()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).bookingDTOList().getFirst().getRideStartTime().toLocalDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void givenAvailableTaxi_whenGetTaxiByStatus_thenReturnAllTaxisByStatus() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPos, yPos);
        taxiService.createTaxi(createTaxiRequest);

        ResponseEntity<TaxiListResponse> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/dashboard/taxis/status?status=" + TaxiStatus.AVAILABLE, TaxiListResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).taxiDTOList()).hasSize(1);
        assertThat(Objects.requireNonNull(response.getBody()).taxiDTOList().getFirst().getTaxiId()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).taxiDTOList().getFirst().getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
    }
}
