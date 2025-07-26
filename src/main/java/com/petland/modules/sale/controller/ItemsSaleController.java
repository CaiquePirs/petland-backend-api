package com.petland.modules.sale.controller;

import com.petland.modules.sale.dtos.ItemsSaleResponseDTO;
import com.petland.modules.sale.mappers.ItemSaleMapper;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.service.ItemsSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/sales/{saleId}/items")
@RequiredArgsConstructor
public class ItemsSaleController {

    private final ItemsSaleService itemsSaleService;
    private final ItemSaleMapper itemSaleMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemsSaleResponseDTO> findItemSaleById(@PathVariable(name = "saleId") UUID saleId,
                                                                 @PathVariable(name = "id") UUID itemSaleId){
        ItemsSale itemsSale = itemsSaleService.findItemBySaleId(saleId, itemSaleId);
        return ResponseEntity.ok().body(itemSaleMapper.toDTO(itemsSale));
    }
}
