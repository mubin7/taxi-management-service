package com.tms.helper;

import com.tms.constant.RideStatus;
import com.tms.constant.TaxiStatus;
import com.tms.dto.BookingDTO;
import com.tms.dto.RideDTO;
import com.tms.dto.TaxiDTO;
import com.tms.payload.request.ride.CompleteRideRequest;
import com.tms.payload.request.ride.CreateRideRequest;
import com.tms.payload.response.booking.BookingListResponse;
import com.tms.payload.response.ride.CompleteRideResponse;
import com.tms.payload.response.ride.CreateRideResponse;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BookingTestHelper {

    public static Booking getBookingWithCoordinates(
            Double srcX, Double srcY, Double destX, Double destY) {
        Booking booking = new Booking();
        booking.setSrcXPos(srcX);
        booking.setSrcYPos(srcY);
        booking.setDestXPos(destX);
        booking.setDestYPos(destY);
        return booking;
    }

    public static void setNewBookingDetails(
            Booking booking, LocalDateTime rideStartTime, Taxi taxi) {
        booking.setRideStatus(RideStatus.IN_PROGRESS);
        booking.setRideStartTime(rideStartTime);
        booking.setTaxi(taxi);
    }

    public static CreateRideRequest getRideRequest(Double srcX, Double srcY, Double destX, Double destY, Double maxDistance) {
        RideDTO rideDTO = new RideDTO(srcX, srcY, destX, destY);
        return new CreateRideRequest(rideDTO, maxDistance);
    }

    public static BookingDTO getBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingId(UUID.randomUUID().toString());
        RideDTO rideDTO = new RideDTO(booking.getSrcXPos(), booking.getSrcYPos(), booking.getDestXPos(), booking.getDestYPos());
        bookingDTO.setRideDTO(rideDTO);
        TaxiDTO taxiDTO = new TaxiDTO();
        taxiDTO.setTaxiId(UUID.randomUUID().toString());
        taxiDTO.setTaxiStatus(booking.getTaxi().getTaxiStatus());
        taxiDTO.setCurrXPos(booking.getTaxi().getCurrXPos());
        taxiDTO.setCurrYPos(booking.getTaxi().getCurrYPos());
        bookingDTO.setTaxiDTO(taxiDTO);
        bookingDTO.setRideStatus(booking.getRideStatus());
        bookingDTO.setRideStartTime(booking.getRideStartTime());
        return bookingDTO;
    }

    public static void setCompleteBookingDetails(Booking booking) {
        Taxi taxi = booking.getTaxi();
        taxi.setTaxiStatus(TaxiStatus.AVAILABLE);
        taxi.setCurrXPos(booking.getDestXPos());
        taxi.setCurrYPos(booking.getDestYPos());
        booking.setTaxi(taxi);
        booking.setRideStatus(RideStatus.COMPLETED);
        booking.setRideStartTime(booking.getRideStartTime().plusDays(1));

    }

    public static CompleteRideRequest getCompleteRideRequest(BookingDTO bookingDTO) {
        return new CompleteRideRequest(bookingDTO);
    }

    public static CreateRideResponse getCreateRideResponse(BookingDTO bookingDTO) {
        return new CreateRideResponse(bookingDTO);
    }

    public static CompleteRideResponse getCompleteRideResponse(BookingDTO bookingDTO) {
        return new CompleteRideResponse(bookingDTO);
    }

    public static BookingListResponse getBookingListResponse(List<BookingDTO> bookingDTOList) {
        return new BookingListResponse(bookingDTOList);
    }
}
