package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.Room;
import com.campusdual.dominiondiamondhotel.model.dao.HotelDao;
import com.campusdual.dominiondiamondhotel.model.dao.RoomDao;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.RoomMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    RoomDao roomDao;

    @InjectMocks
    RoomService roomService;

    @Test
    void addRoomTest(){
        Room room = new Room();
        room.setId(1);
        room.setNumber(1);
        Hotel h = new Hotel();
        h.setId(1);
        room.setHotel_id(h);
        RoomDto createRoomDto = RoomMapper.INSTANCE.toDto(room);

        when(this.roomDao.saveAndFlush(any(Room.class))).thenReturn(room);

        int roomId = roomService.insertRoom(createRoomDto);

        assertEquals(1, roomId);
        verify(this.roomDao, times(1)).saveAndFlush(any(Room.class));
    }

    @Test
    void getAllRoomsTest() {
        List<Room> roomList = new ArrayList<>();
        Hotel hotel = new Hotel();
        hotel.setId(1);
        Room room = new Room();
        room.setHotel_id(hotel);
        roomList.add(room);
        when(this.roomDao.findAll()).thenReturn(roomList);
        List<RoomDto> empList = this.roomService.queryAll();
        verify(this.roomDao, times(1)).findAll();
        assertEquals(1, empList.size());
    }
    @Test
    void updateRoomTest() {
        List<Room> roomList = new ArrayList<>();
        Hotel hotel = new Hotel();
        Room room= new Room();
        hotel.setId(1);
        room.setHotel_id(hotel);
        room.setId(1);
        RoomDto createRoomDto = RoomMapper.INSTANCE.toDto(room);
        roomList.add(room);

        when(this.roomDao.findAll()).thenReturn(roomList);
        when(this.roomDao.saveAndFlush(any(Room.class))).thenReturn(room);

        HttpStatus status = roomService.updateRoom(createRoomDto).getStatusCode();
        int roomIdUpdated = (int) roomService.updateRoom(createRoomDto).getBody();

        assertEquals(1, roomIdUpdated);
        assertEquals(HttpStatus.OK, status);

        verify(this.roomDao, times(2)).findAll();
    }

    @Test
    void manageGetRoom(){

        Room room = new Room();
        Hotel hotel = new Hotel();
        hotel.setId(1);
        room.setId(1);
        room.setNumber(1);
        room.setHotel_id(hotel);

        when(this.roomDao.getReferenceById(1)).thenReturn(room);

        ResponseEntity<?> responseEntity = roomService.manageGetRoom(1);
        HttpStatus status = responseEntity.getStatusCode();
        RoomDto roomDto = (RoomDto) responseEntity.getBody();

        assertEquals(HttpStatus.OK, status);
        assertEquals(1, roomDto.getId());

        verify(this.roomDao, times(1)).getReferenceById(any(Integer.class));

    }

}
