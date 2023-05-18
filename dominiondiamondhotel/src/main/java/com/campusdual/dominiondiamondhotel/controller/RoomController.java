package com.campusdual.dominiondiamondhotel.controller;

import com.campusdual.dominiondiamondhotel.api.IRoomService;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;

    @PostMapping(value = "/add")
    public int addRoom(@RequestBody RoomDto roomDto) {
        return roomService.insertRoom(roomDto);
    }

}
