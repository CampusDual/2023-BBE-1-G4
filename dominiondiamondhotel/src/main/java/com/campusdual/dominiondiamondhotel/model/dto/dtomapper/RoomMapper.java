package com.campusdual.dominiondiamondhotel.model.dto.dtomapper;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.Room;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mapping(source = "hotelId", target = "hotelId", qualifiedByName = "entityToHotelId")
    RoomDto toDto(Room room);
    @Mapping(source = "hotelId", target = "hotelId", qualifiedByName = "hotelIdToEntity")
    Room toEntity(RoomDto roomDto);

    List<RoomDto> toDtoList(List<Room>rooms);

    List<Room>toEntityList(List<RoomDto>roomDtos);

    @Named("hotelIdToEntity")
    default Hotel hotelIdToEntity(int hotelId){

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        return  hotel;

    }

    @Named("entityToHotelId")
    default int entityToHotelId(Hotel hotel){

        return hotel.getId();

    }
}
