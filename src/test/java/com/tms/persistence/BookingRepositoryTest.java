package com.tms.persistence;

import com.tms.constant.RideStatus;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.persistence.repository.TaxiRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class BookingRepositoryTest extends BasePostgresIntegrationTest {

    @Autowired
    private TaxiRepository taxiRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @AfterEach
    public void cleanUp() {
        bookingRepository.deleteAll();
        taxiRepository.deleteAll();
    }

    @Test
    public void whenCreateBooking_thenReturnBooking() {
        Taxi taxi = TaxiRepositoryTestHelper.createTaxi(0.0, 0.0);
        taxiRepository.save(taxi);

        Booking booking = BookingRepositoryTestHelper.createBooking(taxi);
        Booking persistedBooking = bookingRepository.save(booking);

        assertThat(persistedBooking.getBookingId()).isNotNull();
        assertThat(persistedBooking.getSrcXPos()).isNotNull();
        assertThat(persistedBooking.getSrcYPos()).isNotNull();
        assertThat(persistedBooking.getDestXPos()).isNotNull();
        assertThat(persistedBooking.getDestYPos()).isNotNull();
        assertThat(persistedBooking.getRideStatus()).isEqualTo(RideStatus.IN_PROGRESS);
        assertThat(persistedBooking.getRideStartTime()).isNotNull();
    }
}
