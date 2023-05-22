package com.campusdual.dominiondiamondhotel.service;


import com.campusdual.dominiondiamondhotel.model.Customer;
import com.campusdual.dominiondiamondhotel.model.Hotel;
import com.campusdual.dominiondiamondhotel.model.dao.CustomerDao;
import com.campusdual.dominiondiamondhotel.model.dto.CustomerDto;
import com.campusdual.dominiondiamondhotel.model.dto.dtomapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    CustomerDao customerDao;

    @InjectMocks
    CustomerService customerService;

    @Test
    void addCustomerTest() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Asier");
        customer.setLastname1("Asorey");
        customer.setLastname2("Prado");
        customer.setPhone("+3411111111");
        customer.setDni("11111111X");
        customer.setMail("asierasorey@gmail.com");

        CustomerDto createCustomerDto = CustomerMapper.INSTANCE.toDto(customer);
        when(this.customerDao.saveAndFlush(any(Customer.class))).thenReturn(customer);
        int customerId = customerService.insertCustomer(createCustomerDto);
        assertEquals(1, customerId);
        verify(this.customerDao, times(1)).saveAndFlush(any(Customer.class));
    }

    @Test
    void deleteCustomerTest() {
        List<Customer> customerList = new ArrayList<>();
        Customer c = new Customer();
        c.setId(1);
        customerList.add(c);
        CustomerDto customerDto = CustomerMapper.INSTANCE.toDto(c);

        when(this.customerDao.findAll()).thenReturn(customerList);
        ResponseEntity<?> responseEntity = customerService.deleteCustomer(customerDto);
        HttpStatus status = responseEntity.getStatusCode();
        int deletedCustomer = Integer.parseInt(responseEntity.getBody() != null ? responseEntity.getBody().toString() : "-1");
        verify(this.customerDao, times(1)).findAll();
        verify(this.customerDao, times(1)).delete(any(Customer.class));
        assertEquals(1, deletedCustomer);
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void updateCustomerTest() {
        List<Customer> customerList = new ArrayList<>();
        Customer c = new Customer();
        c.setId(1);
        customerList.add(c);
        CustomerDto customerDto = CustomerMapper.INSTANCE.toDto(c);

        when(this.customerDao.findAll()).thenReturn(customerList);
        ResponseEntity<?> responseEntity = customerService.updateCustomer(customerDto);
        HttpStatus status = responseEntity.getStatusCode();
        int updatedCustomerId = Integer.parseInt(responseEntity.getBody() != null ? responseEntity.getBody().toString() : "-1");
        verify(this.customerDao, times(1)).findAll();
        verify(this.customerDao, times(1)).saveAndFlush(any(Customer.class));
        assertEquals(1, updatedCustomerId);
        assertEquals(HttpStatus.OK, status);
    }
}
