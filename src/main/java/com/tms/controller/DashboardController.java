package com.tms.controller;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.service.BookingService;
import com.tms.service.TaxiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final BookingService bookingService;
    private final TaxiService taxiService;

    public DashboardController(BookingService bookingService, TaxiService taxiService) {
        this.bookingService = bookingService;
        this.taxiService = taxiService;
    }

    @GetMapping("/dashboard/bookings")
    public ResponseEntity<BookingListResponse> getBookings() {
        BookingListResponse bookingListResponse = bookingService.getBookings();
        return new ResponseEntity<>(bookingListResponse, HttpStatus.OK);
    }

    @GetMapping("/dashboard/bookings/status")
    public ResponseEntity<BookingListResponse> getBookingsByRideStatus(@RequestParam("status") RideStatus rideStatus) {
        BookingListResponse bookingListResponse = bookingService.getBookingsByRideStatus(rideStatus);
        return new ResponseEntity<>(bookingListResponse, HttpStatus.OK);
    }

    @GetMapping("/dashboard/bookings/date")
    public ResponseEntity<BookingListResponse> getBookingsByDate(@RequestParam("date") String date) {
        BookingListResponse bookingListResponse = bookingService.getBookingsByDate(date);
        return new ResponseEntity<>(bookingListResponse, HttpStatus.OK);
    }

    @GetMapping("/dashboard/taxis/status")
    public ResponseEntity<TaxiListResponse> getTaxisByStatus(@RequestParam("status") TaxiStatus taxiStatus) {
        TaxiListResponse taxiListResponse = taxiService.getTaxiByStatus(taxiStatus);
        return new ResponseEntity<>(taxiListResponse, HttpStatus.OK);
    }

}
