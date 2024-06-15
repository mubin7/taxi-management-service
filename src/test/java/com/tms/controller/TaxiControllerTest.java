package com.tms.controller;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiUpdateResponse;
import com.tms.persistence.BasePostgresIntegrationTest;
import com.tms.persistence.repository.TaxiRepository;
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
public class TaxiControllerTest extends BasePostgresIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private TaxiService taxiService;

    @AfterEach
    public void cleanUp() {
        taxiRepository.deleteAll();
    }

    @Test
    public void whenGetAllTaxis_thenReturnAllTaxis() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(xPos, yPos);
        taxiService.createTaxi(createTaxiRequest);

        ResponseEntity<TaxiListResponse> response = restTemplate
                .getForEntity("http://localhost:" + port + "/taxis", TaxiListResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).taxiDTOList()).hasSize(1);
        assertThat(Objects.requireNonNull(response.getBody()).taxiDTOList().getFirst().getTaxiId()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).taxiDTOList().getFirst().getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
    }

    @Test
    public void whenGetTaxiById_thenReturnTaxiById() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(xPos, yPos);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        String taxiId = createTaxiResponse.taxiDTO().getTaxiId();

        ResponseEntity<TaxiDTO> response = restTemplate
                .getForEntity("http://localhost:" + port + "/taxis/" + taxiId, TaxiDTO.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).getTaxiId()).isEqualTo(taxiId);
    }

    @Test
    public void whenCreateTaxi_thenReturnTaxi() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(xPos, yPos);

        ResponseEntity<CreateTaxiResponse> response = restTemplate
                .postForEntity("http://localhost:" + port + "/taxis", createTaxiRequest, CreateTaxiResponse.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenUpdateTaxi_thenReturnUpdatedTaxi() {
        double xPos = 0.0;
        double yPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(xPos, yPos);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);

        TaxiUpdateRequest taxiUpdateRequest = TaxiServiceTestHelper.taxiUpdateRequest(createTaxiResponse.taxiDTO(), TaxiStatus.NOT_AVAILABLE, xPos, yPos);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<TaxiUpdateRequest> request = new HttpEntity<>(taxiUpdateRequest, headers);

        ResponseEntity<TaxiUpdateResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/taxis",
                HttpMethod.PUT,
                request,
                TaxiUpdateResponse.class
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(Objects.requireNonNull(response.getBody()).taxiDTO().getTaxiStatus()).isEqualTo(TaxiStatus.NOT_AVAILABLE);
    }

}
