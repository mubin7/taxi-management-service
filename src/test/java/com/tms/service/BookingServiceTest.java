package com.tms.service;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.exception.InvalidDateFormatException;
import com.tms.exception.NoBookingRecordFoundException;
import com.tms.helper.BookingTestHelper;
import com.tms.helper.TaxiTestHelper;
import com.tms.mapper.BookingModelMapper;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;
import com.tms.persistence.repository.BookingRepository;
import com.tms.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class BookingServiceTest {

    private BookingService bookingService;
    private BookingRepository bookingRepository;
    private BookingModelMapper bookingModelMapper;

    private Taxi taxi;
    private Booking booking;

    @BeforeEach
    public void setup() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        bookingModelMapper = Mockito.mock(BookingModelMapper.class);
        bookingService = new BookingServiceImpl(bookingRepository, bookingModelMapper);

        double xPos = 0.0;
        double yPos = 0.0;
        taxi = TaxiTestHelper.getTaxiWithStatusAndPosition(TaxiStatus.AVAILABLE, xPos, yPos);
        booking = BookingTestHelper.getBookingWithCoordinates(1.0, 1.0, 3.0, 3.0);
        BookingTestHelper.setNewBookingDetails(booking, LocalDateTime.now(), taxi);
    }

    @Test
    public void givenBookingRecord_whenGetBookings_thenReturnAllBookings() {
        BDDMockito.given(bookingRepository.findAll()).willReturn(Collections.singletonList(booking));
        BDDMockito.given(bookingModelMapper.getModelList(any())).willReturn(Collections.singletonList(BookingTestHelper.getBookingDTO(booking)));

        BookingListResponse bookingListResponse = bookingService.getBookings();
        assertThat(bookingListResponse).isNotNull();
        assertThat(bookingListResponse.bookingDTOList()).hasSize(1);
    }

    @Test
    public void givenNoBookingRecord_whenGetBookings_thenThrowNoBookingRecordFoundException() {
        BDDMockito.given(bookingRepository.findAll()).willReturn(Collections.emptyList());
        BDDMockito.given(bookingModelMapper.getModelList(any())).willReturn(Collections.singletonList(BookingTestHelper.getBookingDTO(booking)));

        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookings());
    }

    @Test
    public void givenInProgressBookingRecord_whenGetBookingsByRideStatus_thenReturnBookingsByRideStatus() {
        BDDMockito.given(bookingRepository.findByRideStatus(any())).willReturn(Collections.singletonList(booking));
        BDDMockito.given(bookingModelMapper.getModelList(any())).willReturn(Collections.singletonList(BookingTestHelper.getBookingDTO(booking)));
        RideStatus rideStatus = RideStatus.IN_PROGRESS;
        BookingListResponse bookingListResponse = bookingService.getBookingsByRideStatus(rideStatus);
        assertThat(bookingListResponse).isNotNull();
        assertThat(bookingListResponse.bookingDTOList()).hasSize(1);
        assertThat(bookingListResponse.bookingDTOList().getFirst().getRideStatus()).isEqualTo(rideStatus);
    }

    @Test
    public void givenNoBookingRecord_whenGetBookingsByRideStatus_thenThrowNoBookingRecordFoundException() {
        BDDMockito.given(bookingRepository.findByRideStatus(any())).willReturn(Collections.emptyList());
        BDDMockito.given(bookingModelMapper.getModelList(any())).willReturn(Collections.singletonList(BookingTestHelper.getBookingDTO(booking)));

        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookingsByRideStatus(RideStatus.IN_PROGRESS));
    }

    @Test
    public void givenInProgressBookingRecord_whenGetBookingsByDate_thenReturnBookingsByDate() {
        BDDMockito.given(bookingRepository.findByRideStartTimeBetween(any(), any())).willReturn(Collections.singletonList(booking));
        BDDMockito.given(bookingModelMapper.getModelList(any())).willReturn(Collections.singletonList(BookingTestHelper.getBookingDTO(booking)));
        String date = LocalDate.now().toString();
        BookingListResponse bookingListResponse = bookingService.getBookingsByDate(date);
        assertThat(bookingListResponse).isNotNull();
        assertThat(bookingListResponse.bookingDTOList()).hasSize(1);
        assertThat(bookingListResponse.bookingDTOList().getFirst().getRideStartTime().toLocalDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void givenInProgressBookingRecord_whenGetBookingsByInvalidDate_thenThrowInvalidDateFormatException() {
        String date = LocalDate.now() + "-";
        assertThrows(InvalidDateFormatException.class, () -> bookingService.getBookingsByDate(date));
    }

    @Test
    public void givenNoBookingRecord_whenGetBookingsByDate_thenThrowNoBookingRecordFoundException() {
        BDDMockito.given(bookingRepository.findByRideStartTimeBetween(any(), any())).willReturn(Collections.emptyList());
        String date = LocalDate.now().toString();
        assertThrows(NoBookingRecordFoundException.class, () -> bookingService.getBookingsByDate(date));
    }
}
