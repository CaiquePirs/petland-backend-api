package com.petland.modules.customer.mappers;

import com.petland.modules.customer.controller.dto.CustomerRequestDTO;
import com.petland.modules.customer.controller.dto.CustomerResponseDTO;
import com.petland.modules.customer.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO toDTO(Customer customer);
}
