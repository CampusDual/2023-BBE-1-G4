package com.campusdual.dominiondiamondhotel.model.dto.dtomapper;


import com.campusdual.dominiondiamondhotel.model.Customer;
import com.campusdual.dominiondiamondhotel.model.dto.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);

    List<CustomerDto> toDtoList(List<Customer>customers);

    List<Customer>toEntityList(List<CustomerDto>customerDtos);
}
