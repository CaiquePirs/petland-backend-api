package com.petland.modules.product.mappers;

import com.petland.modules.product.controller.dto.ProductRequestDTO;
import com.petland.modules.product.controller.dto.ProductResponseDTO;
import com.petland.modules.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO productRequestDTO);

    @Mapping(source = "employee.id", target = "employerId")
    ProductResponseDTO toDTO(Product product);
}
