package com.campusdual.dominiondiamondhotel.model.dto.dtomapper;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.Room;
import com.campusdual.dominiondiamondhotel.model.State;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mapping(source = "hotelId", target = "hotelId", qualifiedByName = "entityToHotelId")
    @Mapping(source = "stateId", target = "stateId", qualifiedByName = "entityToStateId")
    RoomDto toDto(Room room);

    @Mapping(source = "hotelId", target = "hotelId", qualifiedByName = "hotelIdToEntity")
    @Mapping(source = "stateId", target = "stateId", qualifiedByName = "stateIdToEntity")
    Room toEntity(RoomDto roomDto);

    List<RoomDto> toDtoList(List<Room> rooms);

    List<Room> toEntityList(List<RoomDto> roomDtos);

    @Named("hotelIdToEntity")
    default Hotel hotelIdToEntity(int hotelId) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        return hotel;
    }

    @Named("entityToHotelId")
    default int entityToHotelId(Hotel hotel) {
        return hotel.getId();
    }

    @Named("entityToStateId")
    default int entityToStateId(State state){
        return state.getId();
    }

    @Named("stateIdToEntity")
    default State stateIdToEntity(int id){
        State state = new State();
        state.setId(id);
        return state;
    }
}
