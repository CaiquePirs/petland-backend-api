package com.petland.modules.customer.mappers;

import com.petland.modules.customer.dto.CustomerRequestDTO;
import com.petland.modules.customer.dto.CustomerResponseDTO;
import com.petland.modules.customer.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toEntity(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO toDTO(Customer customer);
}
