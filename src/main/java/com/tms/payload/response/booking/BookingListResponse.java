package com.tms.payload.response.booking;

import com.tms.dto.BookingDTO;

import java.util.List;

public record BookingListResponse(List<BookingDTO> bookingDTOList) {
}
