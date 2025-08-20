package com.petland.modules.sale.controller;

import com.petland.modules.sale.dtos.ItemsSaleResponseDTO;
import com.petland.modules.sale.mappers.ItemSaleMapper;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.service.ItemsSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sales/{saleId}/items")
@RequiredArgsConstructor
public class ItemsSaleController {

    private final ItemsSaleService itemsSaleService;
    private final ItemSaleMapper itemSaleMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ItemsSaleResponseDTO> findItemById(@PathVariable(name = "saleId") UUID saleId,
                                                             @PathVariable(name = "id") UUID itemId){
        ItemsSale itemsSale = itemsSaleService.findActiveItemInActiveSale(saleId, itemId);
        return ResponseEntity.ok(itemSaleMapper.toDTO(itemsSale));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateItemById(@PathVariable(name = "saleId") UUID saleId,
                                                   @PathVariable(name = "id") UUID itemId){
        itemsSaleService.deactivateActiveItemInActiveSale(saleId, itemId);
        return ResponseEntity.noContent().build();
    }
}
