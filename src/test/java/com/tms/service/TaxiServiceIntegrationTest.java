package com.tms.service;

import com.tms.BaseIntegrationTest;
import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiRecordFoundException;
import com.tms.exception.TaxiCreationException;
import com.tms.exception.TaxiUpdateException;
import com.tms.helper.TaxiTestHelper;
import com.tms.payload.request.taxi.CreateTaxiRequest;
import com.tms.payload.request.taxi.TaxiUpdateRequest;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiUpdateResponse;
import com.tms.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
public class TaxiServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TaxiService taxiService;

    @Autowired
    private TaxiRepository taxiRepository;
    private CreateTaxiRequest createTaxiRequest;

    @AfterEach
    public void cleanUp() {
        taxiRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        ;
    }

    @Test
    public void whenCreateTaxi_thenReturnTaxi() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        TaxiDTO taxiDTO = createTaxiResponse.taxiDTO();

        assertThat(taxiDTO).isNotNull();
        assertThat(taxiDTO.getTaxiId()).isNotNull();
        assertThat(taxiDTO.getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
        assertThat(taxiDTO.getCurrXPos()).isEqualTo(xPosition);
        assertThat(taxiDTO.getCurrYPos()).isEqualTo(yPosition);
    }

    @Test
    public void whenCreateTaxiWithEmptyTaxiDetails_thenThrowsTaxiCreationException() {
        CreateTaxiRequest createTaxiRequest = new CreateTaxiRequest(null);
        assertThrows(TaxiCreationException.class, () -> taxiService.createTaxi(createTaxiRequest));
    }

    @Test
    public void whenCreateTaxiWithEmptyXPosition_thenThrowsTaxiCreationException() {
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(null, yPosition);
        assertThrows(TaxiCreationException.class, () -> taxiService.createTaxi(createTaxiRequest));
    }

    @Test
    public void whenCreateTaxiWithEmptyYPosition_thenThrowsTaxiCreationException() {
        double xPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, null);
        assertThrows(TaxiCreationException.class, () -> taxiService.createTaxi(createTaxiRequest));
    }

    @Test
    public void whenCreateTaxis_thenReturnAllTaxi() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        taxiService.createTaxi(createTaxiRequest);

        TaxiListResponse taxiListResponse = taxiService.getTaxis();
        assertThat(taxiListResponse.taxiDTOList()).hasSize(1);
    }

    @Test
    public void whenNotCreateTaxis_thenThrowsNoTaxiRecordFoundException() {
        assertThrows(NoTaxiRecordFoundException.class, () -> taxiService.getTaxis());
    }

    @Test
    public void whenCreateTaxi_thenFindTaxiById() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        String taxiId = createTaxiResponse.taxiDTO().getTaxiId();
        TaxiDTO taxiDTO = taxiService.getTaxi(taxiId);

        assertThat(taxiDTO).isNotNull();
        assertThat(taxiDTO.getTaxiId()).isEqualTo(taxiId);
        assertThat(taxiDTO.getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
        assertThat(taxiDTO.getCurrXPos()).isEqualTo(xPosition);
        assertThat(taxiDTO.getCurrYPos()).isEqualTo(yPosition);
    }

    @Test
    public void whenCreateTaxi_thenFindTaxiByInvalidIdThrowsNoTaxiRecordFoundException() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        String taxiId = createTaxiResponse.taxiDTO().getTaxiId();

        assertThrows(NoTaxiRecordFoundException.class, () -> taxiService.getTaxi(taxiId + "1"));
    }

    @Test
    public void whenCreateTaxi_thenFindTaxiByStatus() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        taxiService.createTaxi(createTaxiRequest);
        TaxiListResponse taxiListResponse = taxiService.getTaxiByStatus(TaxiStatus.AVAILABLE);

        assertThat(taxiListResponse.taxiDTOList().getFirst().getTaxiStatus()).isEqualTo(TaxiStatus.AVAILABLE);
    }

    @Test
    public void whenCreateTaxi_thenFindTaxiByInvalidStatusThrowsNoTaxiRecordFoundException() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        taxiService.createTaxi(createTaxiRequest);

        assertThrows(NoTaxiRecordFoundException.class, () -> taxiService.getTaxiByStatus(TaxiStatus.NOT_AVAILABLE));
    }

    @Test
    public void whenUpdateTaxi_thenReturnUpdatedTaxiDetails() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        TaxiDTO taxiDTO = createTaxiResponse.taxiDTO();
        TaxiUpdateRequest taxiUpdateRequest = TaxiTestHelper
                .taxiUpdateRequest(taxiDTO, TaxiStatus.NOT_AVAILABLE, xPosition, yPosition);

        TaxiUpdateResponse taxiUpdateResponse = taxiService.updateTaxi(taxiUpdateRequest);
        TaxiDTO updateTaxiDTO = taxiUpdateResponse.taxiDTO();

        assertThat(taxiUpdateResponse).isNotNull();
        assertThat(updateTaxiDTO.getTaxiId()).isEqualTo(taxiDTO.getTaxiId());
        assertThat(updateTaxiDTO.getTaxiStatus()).isEqualTo(TaxiStatus.NOT_AVAILABLE);
        assertThat(updateTaxiDTO.getCurrXPos()).isEqualTo(xPosition);
        assertThat(updateTaxiDTO.getCurrYPos()).isEqualTo(yPosition);
    }

    @Test
    public void whenUpdateTaxiWithInvalidStatus_thenThrowsTaxiUpdateException() {
        double xPosition = 0.0;
        double yPosition = 0.0;
        CreateTaxiRequest createTaxiRequest = TaxiTestHelper.createTaxiRequest(xPosition, yPosition);
        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(createTaxiRequest);
        TaxiDTO taxiDTO = createTaxiResponse.taxiDTO();
        TaxiUpdateRequest taxiUpdateRequest = TaxiTestHelper
                .taxiUpdateRequest(taxiDTO, TaxiStatus.BOOKED, xPosition, yPosition);

        assertThrows(TaxiUpdateException.class, () -> taxiService.updateTaxi(taxiUpdateRequest));
    }


}
