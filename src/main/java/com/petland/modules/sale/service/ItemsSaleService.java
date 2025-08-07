package com.petland.modules.sale.service;

import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.dtos.ItemsSaleRequestDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.ItemsSaleRepository;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.modules.sale.calculator.SaleCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public List<ItemsSale> createItems(Sale sale, List<ItemsSaleRequestDTO> itemsSaleRequestDTO) {
        List<ItemsSale> listItemsSale = itemsSaleRequestDTO.stream().map(itemsSaleRequest -> {
            Product product = productService.findById(itemsSaleRequest.productId());
            productService.updateStock(itemsSaleRequest.productQuantity(), product.getId());

            BigDecimal totalItemsSale = saleCalculator.calculateTotalSale(itemsSaleRequest.productQuantity(), product.getCostSale());
            BigDecimal profitByItemSale = saleCalculator.calculateProfitByItemSale(product, itemsSaleRequest.productQuantity());

            return ItemsSale.builder()
                    .sale(sale)
                    .product(product)
                    .productPrice(product.getCostSale())
                    .productQuantity(itemsSaleRequest.productQuantity())
                    .itemsSaleTotal(totalItemsSale)
                    .profit(profitByItemSale)
                    .build();
        }).collect(Collectors.toList());
        return listItemsSale;
    }

    public ItemsSale findActiveItemInActiveSale(UUID saleId, UUID itemId){
        Sale sale = saleRepository.findById(saleId)
                .filter(s -> !s.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        return sale.getItemsSale().stream()
                .filter(i -> i.getId().equals(itemId) && !i.getStatus().equals(StatusEntity.DELETED))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("ItemSale with ID " + itemId + " not found in Sale " + saleId));
    }

    public void deactivateItemsList(List<ItemsSale> itemsSalesList){
        if(!itemsSalesList.isEmpty()){
           for(ItemsSale itemsSale : itemsSalesList){
               itemsSale.setStatus(StatusEntity.DELETED);
           }
           itemsSaleRepository.saveAll(itemsSalesList);
        }
    }

    public void deactivateActiveItemInActiveSale(UUID saleId, UUID itemId){
        ItemsSale item = findActiveItemInActiveSale(saleId, itemId);
        item.setStatus(StatusEntity.DELETED);
        itemsSaleRepository.save(item);
    }

    public List<ItemsSale> findAllItemsSaleByProductId(UUID productId){
        return itemsSaleRepository.findByProductId(productId);
    }
}
