package com.campusdual.dominiondiamondhotel.model.dao;

import com.campusdual.dominiondiamondhotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelDao extends JpaRepository<Hotel, Integer> {

    List<Hotel> findByNameContainingIgnoreCase (String name);
}
