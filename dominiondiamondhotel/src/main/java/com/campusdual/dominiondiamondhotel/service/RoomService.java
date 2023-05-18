package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.api.IRoomService;
import com.campusdual.dominiondiamondhotel.model.Room;
import com.campusdual.dominiondiamondhotel.model.dao.RoomDao;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("RoomService")
@Lazy
public class RoomService implements IRoomService {
    @Autowired
    private RoomDao roomDao;

    @Override
    public int insertRoom(RoomDto roomDto) {
        Room room = RoomMapper.INSTANCE.toEntity(roomDto);
        roomDao.saveAndFlush(room);
        return room.getId();
    }
}
