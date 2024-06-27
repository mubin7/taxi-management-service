package com.tms.service;

import com.tms.constant.TaxiStatus;
import com.tms.dto.TaxiDTO;
import com.tms.exception.NoTaxiRecordFoundException;
import com.tms.helper.TaxiTestHelper;
import com.tms.mapper.TaxiModelMapper;
import com.tms.payload.response.taxi.CreateTaxiResponse;
import com.tms.payload.response.taxi.TaxiListResponse;
import com.tms.payload.response.taxi.TaxiUpdateResponse;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.TaxiRepository;
import com.tms.service.impl.TaxiServiceImpl;
import com.tms.validation.TaxiValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

public class TaxiServiceTest {

    private TaxiService taxiService;
    private TaxiRepository taxiRepository;

    private TaxiModelMapper taxiModelMapper;

    private TaxiValidationService taxiValidationService;

    private Taxi taxi;

    private TaxiDTO taxiDTO;

    @BeforeEach
    public void setup() {
        taxiRepository = Mockito.mock(TaxiRepository.class);
        taxiModelMapper = Mockito.mock(TaxiModelMapper.class);
        taxiValidationService = Mockito.mock(TaxiValidationService.class);
        taxiService = new TaxiServiceImpl(taxiRepository, taxiModelMapper, taxiValidationService);

        double xPos = 0.0;
        double yPos = 0.0;
        taxi = TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos);
        taxiDTO = TaxiTestHelper.getTaxiDTOWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos);
    }

    @Test
    public void givenValidTaxiDTO_whenCreateTaxi_thenReturnSavedTaxiDTO() {
        doNothing().when(taxiValidationService).validateCreateTaxi(any());
        BDDMockito.given(taxiModelMapper.getEntity(any())).willReturn(taxi);
        BDDMockito.given(taxiRepository.save(any())).willReturn(taxi);
        BDDMockito.given(taxiModelMapper.getDTO(any())).willReturn(taxiDTO);
        taxiDTO.setTaxiId(UUID.randomUUID().toString());

        CreateTaxiResponse createTaxiResponse = taxiService.createTaxi(TaxiTestHelper.getCreateTaxiRequest(taxiDTO));
        assertThat(createTaxiResponse).isNotNull();
        assertThat(createTaxiResponse.taxiDTO()).isNotNull();

        TaxiDTO responseDTO = createTaxiResponse.taxiDTO();
        assertThat(responseDTO.getTaxiId()).isNotNull();
        assertThat(responseDTO.getCurrXPos()).isEqualTo(taxi.getCurrXPos());
        assertThat(responseDTO.getCurrYPos()).isEqualTo(taxi.getCurrYPos());
        assertThat(responseDTO.getTaxiStatus()).isEqualTo(taxi.getTaxiStatus());
    }

    @Test
    public void givenNoTaxiRecord_whenGetTaxis_thenThrowNoTaxiRecordFoundException() {
        BDDMockito.given(taxiRepository.findAll()).willReturn(Collections.emptyList());

        assertThrows(NoTaxiRecordFoundException.class, () -> taxiService.getTaxis());
    }

    @Test
    public void givenTaxiRecord_whenGetTaxis_thenReturnTaxiRecords() {
        BDDMockito.given(taxiRepository.findAll()).willReturn(Collections.singletonList(taxi));
        taxiDTO.setTaxiId(UUID.randomUUID().toString());
        BDDMockito.given(taxiModelMapper.getDTOList(any())).willReturn(Collections.singletonList(taxiDTO));

        TaxiListResponse taxiListResponse = taxiService.getTaxis();

        assertThat(taxiListResponse).isNotNull();
        assertThat(taxiListResponse.taxiDTOList()).isNotEmpty();

        List<TaxiDTO> taxiDTOList = taxiListResponse.taxiDTOList();

        assertThat(taxiDTOList).hasSize(1);

        TaxiDTO responseDTO = taxiDTOList.getFirst();

        assertThat(responseDTO.getTaxiId()).isNotNull();
        assertThat(responseDTO.getTaxiStatus()).isEqualTo(taxi.getTaxiStatus());
        assertThat(responseDTO.getCurrXPos()).isEqualTo(taxi.getCurrXPos());
        assertThat(responseDTO.getCurrYPos()).isEqualTo(taxi.getCurrYPos());
    }

    @Test
    public void givenTaxiRecord_whenGetTaxiById_thenReturnTaxiRecordWithId() {
        BDDMockito.given(taxiRepository.findById(any())).willReturn(Optional.of(taxi));
        String taxiId = UUID.randomUUID().toString();
        taxiDTO.setTaxiId(taxiId);
        BDDMockito.given(taxiModelMapper.getDTO(any())).willReturn(taxiDTO);

        TaxiDTO responseDTO = taxiService.getTaxi(taxiId);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getTaxiId()).isNotNull();
        assertThat(responseDTO.getTaxiId()).isEqualTo(taxiId);
        assertThat(responseDTO.getTaxiStatus()).isEqualTo(taxi.getTaxiStatus());
        assertThat(responseDTO.getCurrXPos()).isEqualTo(taxi.getCurrXPos());
        assertThat(responseDTO.getCurrYPos()).isEqualTo(taxi.getCurrYPos());
    }

    @Test
    public void givenNoTaxiRecord_whenGetTaxiById_thenThrowNoTaxiRecordFoundException() {
        BDDMockito.given(taxiRepository.findById(any())).willReturn(Optional.empty());
        String taxiId = UUID.randomUUID().toString();

        assertThrows(NoTaxiRecordFoundException.class, () -> taxiService.getTaxi(taxiId));
    }

    @Test
    public void givenTaxiRecord_whenGetTaxiByStatus_thenReturnTaxiRecordWithStatus() {
        BDDMockito.given(taxiRepository.findByTaxiStatus(any())).willReturn(Collections.singletonList(taxi));
        String taxiId = UUID.randomUUID().toString();
        taxiDTO.setTaxiId(taxiId);
        BDDMockito.given(taxiModelMapper.getDTOList(any())).willReturn(Collections.singletonList(taxiDTO));

        TaxiListResponse taxiListResponse = taxiService.getTaxiByStatus(taxi.getTaxiStatus());

        assertThat(taxiListResponse).isNotNull();
        assertThat(taxiListResponse.taxiDTOList()).isNotNull();
        List<TaxiDTO> taxiDTOList = taxiListResponse.taxiDTOList();

        assertThat(taxiDTOList).hasSize(1);

        TaxiDTO responseDTO = taxiDTOList.getFirst();

        assertThat(responseDTO.getTaxiId()).isNotNull();
        assertThat(responseDTO.getTaxiStatus()).isEqualTo(taxi.getTaxiStatus());
        assertThat(responseDTO.getCurrXPos()).isEqualTo(taxi.getCurrXPos());
        assertThat(responseDTO.getCurrYPos()).isEqualTo(taxi.getCurrYPos());
    }

    @Test
    public void givenNoTaxiRecord_whenGetTaxiByStatus_thenThrowNoTaxiRecordFoundException() {
        BDDMockito.given(taxiRepository.findByTaxiStatus(any())).willReturn(Collections.emptyList());

        assertThrows(NoTaxiRecordFoundException.class, () -> taxiService.getTaxiByStatus(taxi.getTaxiStatus()));
    }

    @Test
    public void givenTaxiRecord_whenUpdateTaxi_thenReturnUpdatedTaxiRecord() {
        doNothing().when(taxiValidationService).validateUpdateTaxi(any());
        BDDMockito.given(taxiRepository.findById(any())).willReturn(Optional.of(taxi));
        BDDMockito.given(taxiRepository.save(any())).willReturn(taxi);
        String taxiId = UUID.randomUUID().toString();
        taxiDTO.setTaxiId(taxiId);
        BDDMockito.given(taxiModelMapper.getDTO(any())).willReturn(taxiDTO);

        TaxiUpdateResponse taxiUpdateResponse = taxiService.updateTaxi(TaxiTestHelper.getTaxiUpdateRequest(taxiDTO));

        assertThat(taxiUpdateResponse).isNotNull();
        assertThat(taxiUpdateResponse.taxiDTO()).isNotNull();
        TaxiDTO responseDTO = taxiUpdateResponse.taxiDTO();

        assertThat(responseDTO.getTaxiId()).isNotNull();
        assertThat(responseDTO.getTaxiStatus()).isEqualTo(taxi.getTaxiStatus());
        assertThat(responseDTO.getCurrXPos()).isEqualTo(taxi.getCurrXPos());
        assertThat(responseDTO.getCurrYPos()).isEqualTo(taxi.getCurrYPos());
    }
}
