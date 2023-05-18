package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.Room;
import com.campusdual.dominiondiamondhotel.model.dao.HotelDao;
import com.campusdual.dominiondiamondhotel.model.dao.RoomDao;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.RoomMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

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

}
