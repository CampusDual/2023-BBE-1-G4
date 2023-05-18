package com.campusdual.dominiondiamondhotel.api;

import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;

import java.util.List;

public interface IHotelService {
    int insertHotel(HotelDto hotelDto);
    List<HotelDto> queryAll();
    int deleteHotel(HotelDto hotelDto);

}
