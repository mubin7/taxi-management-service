package com.tms.validation;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.TaxiCreationException;
import com.tms.exception.TaxiUpdateException;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.persistence.BasePostgresIntegrationTest;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.TaxiService;
import com.tms.service.TaxiServiceTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class TaxiValidationServiceTest extends BasePostgresIntegrationTest {

    @Autowired
    private TaxiValidationService taxiValidationService;

    @Autowired
    private TaxiService taxiService;

    @Autowired
    private TaxiRepository taxiRepository;

    @AfterEach
    public void cleanUp() {
        taxiRepository.deleteAll();
    }

    @Test
    public void whenTaxiUpdateRequestStatusIsBooked_thenThrowsTaxiUpdateException() {
        double currXPos = 0.0;
        double currYPos = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(currXPos, currYPos);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        TaxiDTO taxiDTO = createTaxiResponse.taxiDTO();
        TaxiUpdateRequest taxiUpdateRequest = TaxiServiceTestHelper
                .taxiUpdateRequest(taxiDTO, TaxiStatus.BOOKED, currXPos, currYPos);
        assertThrows(TaxiUpdateException.class, () -> taxiValidationService.validateUpdateTaxi(taxiUpdateRequest));
    }

    @Test
    public void whenCreateTaxiRequestWithNullTaxi_thenThrowsTaxiCreationException() {
        CreateTaxiRequest createTaxiRequest = new CreateTaxiRequest(null);
        assertThrows(TaxiCreationException.class, () -> taxiValidationService.validateCreateTaxi(createTaxiRequest));
    }

    @Test
    public void whenCreateTaxiRequestWithMissingCoordinates_thenThrowsTaxiCreationException() {
        CreateTaxiRequest createTaxiRequest = TaxiServiceTestHelper.createTaxiRequest(null, null);
        assertThrows(TaxiCreationException.class, () -> taxiValidationService.validateCreateTaxi(createTaxiRequest));
    }
}
