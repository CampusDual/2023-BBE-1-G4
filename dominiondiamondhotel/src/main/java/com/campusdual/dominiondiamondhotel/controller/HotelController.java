package com.campusdual.dominiondiamondhotel.controller;

import com.campusdual.dominiondiamondhotel.api.IHotelService;
import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

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

}
