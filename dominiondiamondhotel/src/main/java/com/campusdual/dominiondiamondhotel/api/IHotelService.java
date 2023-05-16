package com.campusdual.dominiondiamondhotel.api;

import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;

import java.util.List;

public interface IHotelService {
    HotelDto queryHotel(HotelDto hotelDto);

    List<HotelDto> queryAll();
    int insertHotel(HotelDto hotelDto);
    /*
    int updateHotel(HotelDto hotelDto);

    int deleteHotel(HotelDto hotelDto);
    */
}
