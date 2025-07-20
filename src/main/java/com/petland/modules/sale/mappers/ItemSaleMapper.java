package com.petland.modules.sale.mappers;

import com.petland.modules.sale.dtos.ItemsSaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemSaleMapper {

    ItemsSaleResponseDTO toDTO(ItemsSale itemsSale);
}
