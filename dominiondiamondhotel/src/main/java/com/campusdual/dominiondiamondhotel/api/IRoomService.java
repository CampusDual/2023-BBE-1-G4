package com.campusdual.dominiondiamondhotel.api;

import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;

import java.util.List;

public interface IRoomService {

    int insertRoom(RoomDto roomDto);
    List<RoomDto> queryAll();

}
