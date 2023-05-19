package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.api.IHotelService;
import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.dao.HotelDao;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {
    @Autowired
    private HotelDao hotelDao;

    @Override
    public int insertHotel(HotelDto hotelDto) {
        Hotel hotel = HotelMapper.INSTANCE.toEntity(hotelDto);
        hotelDao.saveAndFlush(hotel);
        return hotel.getId();
    }

    @Override
    public List<HotelDto> queryAll() {
        return HotelMapper.INSTANCE.toDtoList(hotelDao.findAll());
    }

    @Override
    public int deleteHotel(HotelDto hotelDto) {
        int id = hotelDto.getId();
        Hotel hotel = HotelMapper.INSTANCE.toEntity(hotelDto);
        hotelDao.delete(hotel);
        return id;
    }

    @Override
    public ResponseEntity<?> updateHotel(HotelDto hotelDto) {
        for (HotelDto hotel : queryAll()) {
            if (hotel.getId() == hotelDto.getId()) {
                int updatedHotelId = insertHotel(hotelDto);
                return ResponseEntity.ok(updatedHotelId);
            }
        }
        return ResponseEntity.badRequest().body("Hotel no encontrado.");
    }

    @Override
    public HotelDto manageGetHotel(HotelDto hotelDto) {

        Hotel hotel = HotelMapper.INSTANCE.toEntity(hotelDto);
        return HotelMapper.INSTANCE.toDto(hotelDao.getReferenceById(hotel.getId()));
    }

}
