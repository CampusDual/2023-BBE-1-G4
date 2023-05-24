package com.campusdual.dominiondiamondhotel.model.dto.dtomapper;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HotelMapper {
    HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);

    HotelDto toDto(Hotel hotel);

    Hotel toEntity(HotelDto hotelDto);

    List<HotelDto> toDtoList(List<Hotel> hoteles);

    List<Hotel> toEntityList(List<HotelDto> hotelDtos);
}
