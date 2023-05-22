package com.campusdual.dominiondiamondhotel.api;

import com.campusdual.dominiondiamondhotel.model.dto.CustomerDto;
import org.springframework.http.ResponseEntity;

public interface ICustomerService {
    int insertCustomer(CustomerDto customerDto);
    ResponseEntity<?> deleteCustomer(CustomerDto customerDto);
    ResponseEntity<?> updateCustomer(CustomerDto customerDto);
}
