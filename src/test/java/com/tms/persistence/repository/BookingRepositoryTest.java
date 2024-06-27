package com.tms.persistence.repository;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TaxiRepository taxiRepository;

    private Booking booking;


    private Taxi taxi;

    @BeforeEach
    public void setup() {
        taxi = taxiRepository.save(TaxiTestHelper.getTaxiWithStatus(TaxiStatus.AVAILABLE));

        double srcX = 0.0;
        double srcY = 0.0;
        double destX = 3.0;
        double destY = 3.0;
        booking = BookingTestHelper.getBookingWithCoordinates(srcX, srcY, destX, destY);

        LocalDateTime rideStartTime = LocalDateTime.now();
        BookingTestHelper.setNewBookingDetails(booking, rideStartTime, taxi);

        booking = bookingRepository.save(booking);
    }

    @AfterEach
    public void cleanup() {
        taxiRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void givenBookingWithRideStatusInProgress_whenSearchByRideStatusInProgress_thenReturnBookingsWithRideStatusInProgress() {
        RideStatus rideStatus = RideStatus.IN_PROGRESS;
        List<Booking> bookingList = bookingRepository.findByRideStatus(rideStatus);

        assertThat(bookingList).isNotEmpty();
        assertThat(bookingList).hasSize(1);

        Booking searchedBooking = bookingList.getFirst();

        assertThat(searchedBooking).isEqualTo(booking);
        assertThat(searchedBooking.getRideStatus()).isEqualTo(rideStatus);
        assertThat(searchedBooking.getBookingId()).isNotNull();
        assertThat(searchedBooking.getRideStartTime()).isNotNull();
        assertThat(searchedBooking.getRideEndTime()).isNull();
        assertThat(searchedBooking.getSrcXPos()).isEqualTo(booking.getSrcXPos());
        assertThat(searchedBooking.getSrcYPos()).isEqualTo(booking.getSrcYPos());
        assertThat(searchedBooking.getDestXPos()).isEqualTo(booking.getDestXPos());
        assertThat(searchedBooking.getDestYPos()).isEqualTo(booking.getDestYPos());
        assertThat(searchedBooking.getTaxi()).isEqualTo(taxi);
    }

    @Test
    public void givenBookingWithRideStatusCompleted_whenSearchByRideStatusCompleted_thenReturnBookingsWithRideStatusCompleted() {
        RideStatus rideStatus = RideStatus.COMPLETED;
        booking.setRideStatus(rideStatus);
        booking.setRideEndTime(booking.getRideStartTime().plusDays(1));
        Booking completedBooking = bookingRepository.save(booking);

        List<Booking> bookingList = bookingRepository.findByRideStatus(rideStatus);

        assertThat(bookingList).isNotEmpty();
        assertThat(bookingList).hasSize(1);

        Booking searchedBooking = bookingList.getFirst();

        assertThat(searchedBooking).isEqualTo(completedBooking);
        assertThat(searchedBooking.getRideStatus()).isEqualTo(rideStatus);
        assertThat(searchedBooking.getBookingId()).isNotNull();
        assertThat(searchedBooking.getRideStartTime()).isNotNull();
        assertThat(searchedBooking.getRideEndTime()).isNotNull();
        assertThat(searchedBooking.getSrcXPos()).isEqualTo(booking.getSrcXPos());
        assertThat(searchedBooking.getSrcYPos()).isEqualTo(booking.getSrcYPos());
        assertThat(searchedBooking.getDestXPos()).isEqualTo(booking.getDestXPos());
        assertThat(searchedBooking.getDestYPos()).isEqualTo(booking.getDestYPos());
        assertThat(searchedBooking.getTaxi()).isEqualTo(taxi);
    }

    @Test
    public void givenBookingWithRideStarDate_whenSearchBookingByDateRange_thenReturnBookingsWithRideStartDate() {
        LocalDateTime startDate = booking.getRideStartTime().toLocalDate().atStartOfDay();
        LocalDateTime endDate = startDate.plusDays(1);
        List<Booking> bookingList = bookingRepository.findByRideStartTimeBetween(startDate, endDate);

        assertThat(bookingList).isNotEmpty();
        assertThat(bookingList).hasSize(1);
        Booking searchedBooking = bookingList.getFirst();

        assertThat(searchedBooking).isEqualTo(booking);
        assertThat(searchedBooking.getRideStartTime()).isAfterOrEqualTo(startDate);
        assertThat(searchedBooking.getRideStartTime()).isBeforeOrEqualTo(endDate);
    }
}
