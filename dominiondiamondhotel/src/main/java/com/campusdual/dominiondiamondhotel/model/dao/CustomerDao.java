package com.campusdual.dominiondiamondhotel.model.dao;

import com.campusdual.dominiondiamondhotel.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer,Integer> {
}
