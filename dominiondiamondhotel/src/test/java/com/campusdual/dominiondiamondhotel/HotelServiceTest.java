package com.campusdual.dominiondiamondhotel;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.dao.HotelDao;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import com.campusdual.dominiondiamondhotel.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

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
    void addHotelTest(){
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Continental");
        HotelDto createHotelDto = HotelMapper.INSTANCE.toDto(hotel);

        when(this.hotelDao.saveAndFlush(any(Hotel.class))).thenReturn(hotel);

        int hotelId = hotelService.insertHotel(createHotelDto);

        assertEquals(1, hotelId);
        verify(this.hotelDao, times(1)).saveAndFlush(any(Hotel.class));
    }
}
