package com.tms.persistence.repository;

import com.tms.constant.TaxiStatus;
import com.tms.helper.TaxiTestHelper;
import com.tms.persistence.entity.Taxi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class TaxiRepositoryTest {

    @Autowired
    private TaxiRepository taxiRepository;

    private Taxi taxi;

    @BeforeEach
    public void setup() {
        double xPos = 0.0;
        double yPos = 0.0;
        taxi = taxiRepository.save(TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos));
    }

    @AfterEach
    public void cleanup() {
        taxiRepository.deleteAll();
    }

    @Test
    public void givenTaxiWithTaxiStatusAvailable_whenSearchByTaxiStatusAvailable_thenReturnTaxisWithTaxiStatusAvailable() {
        TaxiStatus taxiStatus = TaxiStatus.AVAILABLE;
        List<Taxi> taxiList = taxiRepository.findByTaxiStatus(taxiStatus);

        assertThat(taxiList).isNotEmpty();
        assertThat(taxiList).hasSize(1);

        Taxi searchedTaxi = taxiList.getFirst();

        assertThat(searchedTaxi).isEqualTo(taxi);
        assertThat(searchedTaxi.getTaxiId()).isNotNull();
        assertThat(searchedTaxi.getTaxiStatus()).isEqualTo(taxiStatus);
        assertThat(searchedTaxi.getCurrXPos()).isEqualTo(taxi.getCurrXPos());
        assertThat(searchedTaxi.getCurrYPos()).isEqualTo(taxi.getCurrYPos());
        assertThat(searchedTaxi.getBookings()).isNull();
    }
}
