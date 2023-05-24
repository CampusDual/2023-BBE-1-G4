package com.campusdual.dominiondiamondhotel.model.dao;

import com.campusdual.dominiondiamondhotel.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateDao extends JpaRepository<State, Integer> {
}
