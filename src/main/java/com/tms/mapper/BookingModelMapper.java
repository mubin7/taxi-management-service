package com.tms.mapper;

import com.tms.dto.BookingDTO;
import com.tms.dto.TaxiDTO;
import com.tms.persistence.entity.Booking;
import com.tms.persistence.entity.Taxi;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingModelMapper {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaxiModelMapper taxiModelMapper;

    public Booking getEntity(BookingDTO bookingDTO) {
        Taxi taxi = taxiModelMapper.getEntity(bookingDTO.getTaxiDTO());
        Booking booking = modelMapper.map(bookingDTO, Booking.class);
        booking.setTaxi(taxi);
        return booking;
    }

    public BookingDTO getDTO(Booking booking) {
        TaxiDTO taxiDTO = taxiModelMapper.getModel(booking.getTaxi());
        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
        bookingDTO.setTaxiDTO(taxiDTO);
        return bookingDTO;
    }

    public List<BookingDTO> getModelList(List<Booking> bookingList) {
        return bookingList.stream().map(this::getDTO).collect(Collectors.toList());
    }
}
