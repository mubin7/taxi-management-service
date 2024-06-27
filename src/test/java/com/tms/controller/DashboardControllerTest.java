package com.tms.controller;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.RideDTO;
import com.tms.dto.TaxiDTO;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.service.BookingService;
import com.tms.service.TaxiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private TaxiService taxiService;

    private BookingDTO bookingDTO;

    @BeforeEach
    public void setup() {
        RideDTO rideDTO = new RideDTO(1.0, 1.0, 3.0, 3.0);
        bookingDTO = new BookingDTO();
        bookingDTO.setRideDTO(rideDTO);
        bookingDTO.setRideStatus(RideStatus.IN_PROGRESS);
        bookingDTO.setRideStartTime(LocalDateTime.now());
    }

    @Test
    public void givenBookingObject_whenGetBookings_thenReturnBookings() throws Exception {
        given(bookingService.getBookings()).willReturn(BookingTestHelper.getBookingListResponse(Collections.singletonList(bookingDTO)));

        mockMvc.perform(get("/api/dashboard/bookings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBookingObject_whenGetBookingsByStatus_thenReturnBookings() throws Exception {
        given(bookingService.getBookingsByRideStatus(any())).willReturn(BookingTestHelper.getBookingListResponse(Collections.singletonList(bookingDTO)));

        mockMvc.perform(get("/api/dashboard/bookings/status?status={rideStatus}", RideStatus.IN_PROGRESS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenBookingObject_whenGetBookingsByDate_thenReturnBookings() throws Exception {
        given(bookingService.getBookingsByDate(any())).willReturn(BookingTestHelper.getBookingListResponse(Collections.singletonList(bookingDTO)));

        mockMvc.perform(get("/api/dashboard/bookings/date?date={date}", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenTaxiObject_whenGetTaxisByStatus_thenReturnTaxis() throws Exception {
        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setTaxiId(UUID.randomUUID().toString());
        taxiDTO.setTaxiStatus(TaxiStatus.AVAILABLE);
        taxiDTO.setCurrXPos(1.0);
        taxiDTO.setCurrYPos(1.0);
        given(taxiService.getTaxiByStatus(any())).willReturn(TaxiTestHelper.getTaxiListResponse(Collections.singletonList(taxiDTO)));

        mockMvc.perform(get("/api/dashboard/taxis/status?status={status}", taxiDTO.getTaxiStatus())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
