package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.api.IHotelService;
import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.dao.HotelDao;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

}
