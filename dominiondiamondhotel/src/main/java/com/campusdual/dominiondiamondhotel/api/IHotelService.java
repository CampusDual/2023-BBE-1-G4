package com.campusdual.dominiondiamondhotel.api;

import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IHotelService {
    int insertHotel(HotelDto hotelDto);
    List<HotelDto> queryAll();
    int deleteHotel(HotelDto hotelDto);
    ResponseEntity<?> updateHotel(HotelDto hotelDto);
    HotelDto manageGetHotel(HotelDto hotelDto);

    List<HotelDto> getByName(String name);
}
