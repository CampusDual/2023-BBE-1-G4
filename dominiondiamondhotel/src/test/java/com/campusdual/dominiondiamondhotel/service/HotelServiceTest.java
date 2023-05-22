package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.Room;
import com.campusdual.dominiondiamondhotel.model.dao.HotelDao;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import com.campusdual.dominiondiamondhotel.service.HotelService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    HotelDao hotelDao;

    @InjectMocks
    HotelService hotelService;

    @Test
    void addHotelTest() {
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Continental");
        HotelDto createHotelDto = HotelMapper.INSTANCE.toDto(hotel);

        when(this.hotelDao.saveAndFlush(any(Hotel.class))).thenReturn(hotel);

        int hotelId = hotelService.insertHotel(createHotelDto);

        assertEquals(1, hotelId);
        verify(this.hotelDao, times(1)).saveAndFlush(any(Hotel.class));
    }

    @Test
    void getAllHotelsTest() {
        List<Hotel> hotelList = new ArrayList<>();
        hotelList.add(new Hotel());
        when(this.hotelDao.findAll()).thenReturn(hotelList);
        List<HotelDto> empList = this.hotelService.queryAll();
        verify(this.hotelDao, times(1)).findAll();
        assertEquals(1, empList.size());
    }

    @Test
    void deleteHotelTest() {
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Continental");

        HotelDto editHotelDto = HotelMapper.INSTANCE.toDto(hotel);

        Integer deleteHotelId = this.hotelService.deleteHotel(editHotelDto);
        verify(this.hotelDao, times(1)).delete(any(Hotel.class));
        assertNotNull(deleteHotelId);
    }

    @Test
    void updateHotelTest() {
        List<Hotel> hotelList = new ArrayList<>();
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Continental");
        HotelDto createHotelDto = HotelMapper.INSTANCE.toDto(hotel);
        hotelList.add(hotel);

        when(this.hotelDao.findAll()).thenReturn(hotelList);
        when(this.hotelDao.saveAndFlush(any(Hotel.class))).thenReturn(hotel);

        HttpStatus status = hotelService.updateHotel(createHotelDto).getStatusCode();
        int hotelIdUpdated = (int) hotelService.updateHotel(createHotelDto).getBody();

        assertEquals(1, hotelIdUpdated);
        assertEquals(HttpStatus.OK, status);

        verify(this.hotelDao, times(2)).findAll();
    }

    @Test
    void manageGetHotelTest(){

        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Continental");

        when(this.hotelDao.getReferenceById(1)).thenReturn(hotel);

        HotelDto createHotelDto = this.hotelService.manageGetHotel(HotelMapper.INSTANCE.toDto(hotel));

        assertEquals(1, hotel.getId());
        assertEquals("Continental", hotel.getName());

        verify(this.hotelDao, times(1)).getReferenceById(1);

    }
    @Test
    void getByName(){
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        List<Hotel> hotelList = new ArrayList<>();
        hotel1.setId(1);
        hotel1.setName("Continental");
        hotel2.setId(2);
        hotel2.setName("Cont");
        hotelList.add(hotel1);
        hotelList.add(hotel2);

        when(this.hotelDao.findByNameContainingIgnoreCase("Cont")).thenReturn(hotelList);

        List<HotelDto> createHotelDto = this.hotelService.getByName("Cont");

        assertEquals(2,createHotelDto.size());
        verify(this.hotelDao,times(1)).findByNameContainingIgnoreCase("Cont");
    }

}
