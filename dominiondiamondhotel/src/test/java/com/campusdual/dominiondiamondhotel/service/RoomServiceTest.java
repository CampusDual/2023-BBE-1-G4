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
        room.setHotelId(h);
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
        room.setHotelId(hotel);
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
        room.setHotelId(hotel);
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
        room.setHotelId(hotel);

        when(this.roomDao.getReferenceById(1)).thenReturn(room);

        ResponseEntity<?> responseEntity = roomService.manageGetRoom(1);
        HttpStatus status = responseEntity.getStatusCode();
        RoomDto roomDto = (RoomDto) responseEntity.getBody();

        assertEquals(HttpStatus.OK, status);
        assertEquals(1, roomDto.getId());

        verify(this.roomDao, times(1)).getReferenceById(any(Integer.class));

    }

    @Test
    void getRoomsFromHotel(){

        List<Room> rooms = new ArrayList<>();

        Room room = new Room();
        Room room2 = new Room();
        Room room3 = new Room();
        Hotel hotel = new Hotel();
        hotel.setId(1);
        room.setId(1);
        room2.setId(2);
        room3.setId(3);
        room.setNumber(1);
        room2.setNumber(2);
        room3.setNumber(3);
        room.setHotelId(hotel);
        room2.setHotelId(hotel);
        room3.setHotelId(hotel);

        rooms.add(room);
        rooms.add(room2);
        rooms.add(room3);

        when(this.roomDao.findByHotelId(any(Hotel.class))).thenReturn(rooms);

        ResponseEntity<?> responseEntity = roomService.getRoomsFromHotel(any(Integer.class));
        HttpStatus status = responseEntity.getStatusCode();
        List<Room> roomR = (List<Room>) responseEntity.getBody();

        assertEquals(HttpStatus.OK, status);
        assertEquals(3, roomR.size());

        verify(this.roomDao, times(1)).findByHotelId(any(Hotel.class));

    }

}
