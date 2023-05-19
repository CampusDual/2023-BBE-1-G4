package com.campusdual.dominiondiamondhotel.controller;

import com.campusdual.dominiondiamondhotel.api.IHotelService;
import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.dao.HotelDao;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    HotelDao hotelDao;
    @Autowired
    private IHotelService hotelService;

    @PostMapping(value = "/add")
    public int addHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.insertHotel(hotelDto);
    }

    @GetMapping(value = "/getAll")
    public List<HotelDto> getAllHotels() {
        return hotelService.queryAll();
    }

    @DeleteMapping(value = "/delete")
    public int deleteHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.deleteHotel(hotelDto);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateHotel(@RequestBody HotelDto hotelDto) {
        return hotelService.updateHotel(hotelDto);
    }

    @GetMapping(value = "/managerGetHotel/{id}")
    public HotelDto manageGetHotel(@PathVariable int id){
        return hotelService.manageGetHotel(HotelMapper.INSTANCE.toDto(hotelDao.getReferenceById(id)));
    }
}
