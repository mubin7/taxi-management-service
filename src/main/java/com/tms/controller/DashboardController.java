package com.tms.controller;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.service.BookingService;
import com.tms.service.TaxiService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final BookingService bookingService;
    private final TaxiService taxiService;

    public DashboardController(BookingService bookingService, TaxiService taxiService) {
        this.bookingService = bookingService;
        this.taxiService = taxiService;
    }

    @GetMapping("/bookings")
    @Operation(summary = "Gets the complete list of taxi bookings.")
    public ResponseEntity<BookingListResponse> getBookings() {
        BookingListResponse bookingListResponse = bookingService.getBookings();
        return new ResponseEntity<>(bookingListResponse, HttpStatus.OK);
    }

    @GetMapping("/bookings/status")
    @Operation(summary = "Gets the list of bookings by ride status.")
    public ResponseEntity<BookingListResponse> getBookingsByRideStatus(@RequestParam("status") RideStatus rideStatus) {
        BookingListResponse bookingListResponse = bookingService.getBookingsByRideStatus(rideStatus);
        return new ResponseEntity<>(bookingListResponse, HttpStatus.OK);
    }

    @GetMapping("/bookings/date")
    @Operation(summary = "Gets the list of bookings by date using the format yyyy-MM-dd eg 2000-01-01.")
    public ResponseEntity<BookingListResponse> getBookingsByDate(@RequestParam("date") String date) {
        BookingListResponse bookingListResponse = bookingService.getBookingsByDate(date);
        return new ResponseEntity<>(bookingListResponse, HttpStatus.OK);
    }

    @GetMapping("/taxis/status")
    @Operation(summary = "Gets the list of taxis by ride status.")
    public ResponseEntity<TaxiListResponse> getTaxisByStatus(@RequestParam("status") TaxiStatus taxiStatus) {
        TaxiListResponse taxiListResponse = taxiService.getTaxiByStatus(taxiStatus);
        return new ResponseEntity<>(taxiListResponse, HttpStatus.OK);
    }

}
