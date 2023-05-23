package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.api.IRoomService;
import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.Room;
import com.campusdual.dominiondiamondhotel.model.dao.RoomDao;
import com.campusdual.dominiondiamondhotel.model.dto.HotelDto;
import com.campusdual.dominiondiamondhotel.model.dto.RoomDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.HotelMapper;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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

    @Override
    public List<RoomDto> queryAll() {
        return RoomMapper.INSTANCE.toDtoList(roomDao.findAll());
    }

    @Override
    public ResponseEntity<?> updateRoom(RoomDto roomDto) {
        for (RoomDto room : queryAll()) {
            if (room.getId() == roomDto.getId()) {
                int updatedRoomId = insertRoom(roomDto);
                return ResponseEntity.ok(updatedRoomId);
            }
        }
        return ResponseEntity.badRequest().body("Habitación no encontrada.");
    }

    public ResponseEntity<?> manageGetRoom(int id) {

        try{

            return ResponseEntity.ok(RoomMapper.INSTANCE.toDto(roomDao.getReferenceById(id)));

        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

    }
}
