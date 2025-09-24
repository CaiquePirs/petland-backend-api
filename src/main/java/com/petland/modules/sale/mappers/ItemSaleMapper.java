package com.petland.modules.sale.mappers;

import com.petland.modules.sale.controller.dtos.ItemsSaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemSaleMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "sale.id", target = "saleId")
    ItemsSaleResponseDTO toDTO(ItemsSale itemsSale);
}
