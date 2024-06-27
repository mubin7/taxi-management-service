package com.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.dto.BookingDTO;
import com.tms.dto.RideDTO;
import com.tms.helper.BookingTestHelper;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.service.RideService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RideController.class)
public class RideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RideService rideService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDTO bookingDTO;

    private RideDTO rideDTO;

    @BeforeEach
    public void setup() {
        rideDTO = new RideDTO(1.0, 1.0, 3.0, 3.0);
        bookingDTO = new BookingDTO();
        bookingDTO.setRideDTO(rideDTO);
    }

    @Test
    public void givenRideData_whenCreateRideBooking_thenReturnNewRideBooking() throws Exception {

        given(rideService.createRide(any())).willReturn(BookingTestHelper.getCreateRideResponse(bookingDTO));

        mockMvc.perform(post("/api/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateRideRequest(rideDTO, 3.0))))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenExistingRide_whenCompleteRide_thenReturnCompletedRide() throws Exception {

        given(rideService.completeRide(any())).willReturn(BookingTestHelper.getCompleteRideResponse(bookingDTO));

        mockMvc.perform(put("/api/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CompleteRideRequest(bookingDTO))))
                .andExpect(status().isAccepted());
    }
}
