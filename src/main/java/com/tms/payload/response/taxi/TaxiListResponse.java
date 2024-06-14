package com.tms.payload.response.taxi;

import com.tms.dto.TaxiDTO;

import java.util.List;

public record TaxiListResponse(List<TaxiDTO> taxiDTOList) {
}
