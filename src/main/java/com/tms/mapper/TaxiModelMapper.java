package com.tms.mapper;

import com.tms.dto.TaxiDTO;
import com.tms.persistence.entity.Taxi;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaxiModelMapper {

    @Autowired
    private ModelMapper modelMapper;

    public Taxi getEntity(TaxiDTO taxiDTO) {
        return modelMapper.map(taxiDTO, Taxi.class);
    }

    public TaxiDTO getModel(Taxi taxi) {
        return modelMapper.map(taxi, TaxiDTO.class);
    }

    public List<TaxiDTO> getModelList(List<Taxi> taxiList) {
        return modelMapper.map(taxiList, new TypeToken<List<TaxiDTO>>() {
        }.getType());
    }
}
