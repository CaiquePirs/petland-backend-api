package com.petland.modules.sale.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.dtos.ItemsSaleRequestDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.ItemsSaleRepository;
import com.petland.modules.sale.repositories.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemsSaleService {

    private final ProductService productService;
    private final ItemsSaleRepository itemsSaleRepository;
    private final SaleRepository saleRepository;
    private final SaleCalculator saleCalculator;

    @Transactional
    public List<ItemsSale> createItemsSale(Sale sale, List<ItemsSaleRequestDTO> itemsSaleRequestDTO) {
        List<ItemsSale> listItemsSale = itemsSaleRequestDTO.stream().map(itemsSaleRequest -> {
            ItemsSale itemsSale = new ItemsSale();
            itemsSale.setCreateAt(LocalDateTime.now());

            Product product = productService.findById(itemsSaleRequest.productId());
            productService.updateProductStock(itemsSaleRequest.productQuantity(), product.getId());

            BigDecimal totalItemsSale = saleCalculator.calculateTotalSale(
                    itemsSaleRequest.productQuantity(),
                    product.getCostSale()
            );

            itemsSale.setProduct(product);
            itemsSale.setItemsSaleTotal(BigDecimal.valueOf(totalItemsSale));
            itemsSale.setProductPrice(product.getCostSale());
            itemsSale.setSale(sale);
            itemsSale.setProductQuantity(itemsSaleRequest.productQuantity());
            itemsSale.setStatus(StatusEntity.ACTIVE);

            return itemsSaleRepository.save(itemsSale);
        }).collect(Collectors.toList());
        return listItemsSale;
    }

    public ItemsSale findItemBySaleId(UUID saleId, UUID itemId){
        Sale sale = saleRepository.findById(saleId)
                .filter(s -> !s.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        return sale.getItemsSale().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("ItemSale with ID " + itemId + " not found in Sale " + saleId));
    }

    public void deleteItemsList(List<ItemsSale> itemsSalesList){
        if(!itemsSalesList.isEmpty()){
           for(ItemsSale itemsSale : itemsSalesList){
               itemsSale.setStatus(StatusEntity.DELETED);
           }
           itemsSaleRepository.saveAll(itemsSalesList);
        }
    }
}
