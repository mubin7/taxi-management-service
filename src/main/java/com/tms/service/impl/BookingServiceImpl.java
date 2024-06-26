package com.tms.service.impl;

import com.tms.constant.RideStatus;
import com.tms.dto.BookingDTO;
import com.tms.exception.InvalidDateFormatException;
import com.tms.exception.NoBookingRecordFoundException;
import com.tms.mapper.BookingModelMapper;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.repository.BookingRepository;
import com.tms.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final BookingModelMapper bookingModelMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingModelMapper bookingModelMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingModelMapper = bookingModelMapper;
    }

    @Override
    public BookingListResponse getBookings() {
        LOGGER.info("getting the list of all bookings.");
        List<Booking> bookingList = bookingRepository.findAll();
        if (bookingList.isEmpty()) {
            LOGGER.error("No booking record(s) found");
            throw new NoBookingRecordFoundException("No booking record(s) found");
        }
        List<BookingDTO> bookingDTOList = bookingModelMapper.getModelList(bookingList);
        LOGGER.info("booking list retrieved successfully.");
        return new BookingListResponse(bookingDTOList);
    }

    @Override
    public BookingListResponse getBookingsByRideStatus(RideStatus rideStatus) {
        LOGGER.info("getting the list of all bookings by journey status : " + rideStatus);
        List<Booking> bookingList = bookingRepository.findByRideStatus(rideStatus);
        if (bookingList.isEmpty()) {
            LOGGER.error("No booking record(s) found for the status : {}", rideStatus);
            throw new NoBookingRecordFoundException("No booking record(s) found for the status : " + rideStatus);
        }
        List<BookingDTO> bookingDTOList = bookingModelMapper.getModelList(bookingList);
        LOGGER.info("booking list by journey status retrieved successfully.");
        return new BookingListResponse(bookingDTOList);
    }

    @Override
    public BookingListResponse getBookingsByDate(String date) {
        LOGGER.info("getting the list of all bookings by date : " + date);
        LocalDate bookingDate;
        try {
            bookingDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            LOGGER.error("Please use yyyy-MM-dd format for searching bookings by date. eg 2000-01-01");
            throw new InvalidDateFormatException("Please use yyyy-MM-dd format for searching bookings by date. eg 2000-01-01.");
        }

        List<Booking> bookingList = bookingRepository.findByRideStartTimeBetween(
                bookingDate.atStartOfDay(), bookingDate.plusDays(1).atStartOfDay());
        if (bookingList.isEmpty()) {
            LOGGER.error("No booking record(s) found for the date : {}", date);
            throw new NoBookingRecordFoundException("No booking record(s) found for the date : " + date);
        }
        List<BookingDTO> bookingDTOList = bookingModelMapper.getModelList(bookingList);
        LOGGER.info("booking list by date retrieved successfully.");
        return new BookingListResponse(bookingDTOList);
    }
}
