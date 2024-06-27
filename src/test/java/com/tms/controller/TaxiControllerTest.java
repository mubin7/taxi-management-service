package com.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.helper.TaxiTestHelper;
import com.tms.service.TaxiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaxiController.class)
public class TaxiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaxiService taxiService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaxiDTO taxiDTO;

    @BeforeEach
    public void setup() {
        taxiDTO = new TaxiDTO();
        taxiDTO.setTaxiId(UUID.randomUUID().toString());
        taxiDTO.setTaxiStatus(TaxiStatus.AVAILABLE);
        taxiDTO.setCurrXPos(1.0);
        taxiDTO.setCurrYPos(1.0);
    }

    @Test
    public void givenTaxiObject_whenGetTaxis_thenReturnTaxiList() throws Exception {
        given(taxiService.getTaxis()).willReturn(TaxiTestHelper.getTaxiListResponse(Collections.singletonList(taxiDTO)));

        mockMvc.perform(get("/api/taxis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenTaxiObject_whenGetTaxiById_thenReturnTaxi() throws Exception {

        given(taxiService.getTaxi(any())).willReturn(taxiDTO);

        mockMvc.perform(get("/api/taxis/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenTaxiObject_whenCreateTaxi_thenReturnNewTaxi() throws Exception {

        given(taxiService.createTaxi(any())).willReturn(TaxiTestHelper.getCreateTaxiResponse(taxiDTO));

        mockMvc.perform(post("/api/taxis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TaxiTestHelper.getCreateTaxiRequest(taxiDTO))))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenTaxiObject_whenUpdateTaxi_thenReturnUpdatedTaxi() throws Exception {

        given(taxiService.updateTaxi(any())).willReturn(TaxiTestHelper.getTaxiUpdateResponse(taxiDTO));

        mockMvc.perform(put("/api/taxis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TaxiTestHelper.getCreateTaxiRequest(taxiDTO))))
                .andExpect(status().isOk());
    }
}
