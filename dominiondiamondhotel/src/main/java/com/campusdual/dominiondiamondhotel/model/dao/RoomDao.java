package com.campusdual.dominiondiamondhotel.model.dao;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface RoomDao extends JpaRepository<Room, Integer> {
    List<Room> findByHotelId(Hotel hotel);
}