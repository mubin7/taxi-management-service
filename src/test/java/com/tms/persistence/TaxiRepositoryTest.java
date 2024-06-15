package com.tms.persistence;

import com.tms.constant.TaxiStatus;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class TaxiRepositoryTest extends BasePostgresIntegrationTest {

    @Autowired
    private TaxiRepository taxiRepository;

    @AfterEach
    public void cleanUp() {
        taxiRepository.deleteAll();
    }

    @Test
    public void whenCreateTaxi_thenReturnTaxi() {
        int taxiCount = 2;
        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(taxiCount);
        taxiRepository.saveAll(taxiList);

        List<Taxi> persistedTaxiList = taxiRepository.findAll();
        assertThat(persistedTaxiList).hasSize(taxiCount);
    }

    @Test
    public void whenCreateTaxiWithStatus_thenReturnTaxiWithStatus() {
        int taxiCount = 2;
        TaxiStatus taxiStatus = TaxiStatus.AVAILABLE;
        List<Taxi> taxiList = TaxiRepositoryTestHelper.createTaxis(taxiCount, taxiStatus);
        taxiRepository.saveAll(taxiList);

        List<Taxi> persistedTaxiList = taxiRepository.findByTaxiStatus(taxiStatus);
        assertThat(persistedTaxiList).hasSize(taxiCount);
        assertThat(persistedTaxiList.getFirst().getTaxiId()).isNotNull();
        assertThat(persistedTaxiList.getFirst().getCurrXPos()).isNotNull();
        assertThat(persistedTaxiList.getFirst().getCurrYPos()).isNotNull();
        assertThat(persistedTaxiList.getFirst().getTaxiStatus()).isEqualTo(taxiStatus);
    }
}
