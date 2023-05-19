package com.campusdual.dominiondiamondhotel.service;

import com.campusdual.dominiondiamondhotel.api.ICustomerService;
import com.campusdual.dominiondiamondhotel.model.Customer;
import com.campusdual.dominiondiamondhotel.model.dao.CustomerDao;
import com.campusdual.dominiondiamondhotel.model.dto.CustomerDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("CustomerService")
@Lazy
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public int insertCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.INSTANCE.toEntity(customerDto);
        customerDao.saveAndFlush(customer);
        return customer.getId();
    }

    @Override
    public ResponseEntity<?> deleteCustomer(CustomerDto customerDto) {
        for (Customer c : customerDao.findAll()) {
            if (c.getId() == CustomerMapper.INSTANCE.toEntity(customerDto).getId()) {
                customerDao.delete(CustomerMapper.INSTANCE.toEntity(customerDto));
                return ResponseEntity.ok(String.valueOf(c.getId()));
            }
        }
        return ResponseEntity.badRequest().body("Customer not found.");
    }
}
