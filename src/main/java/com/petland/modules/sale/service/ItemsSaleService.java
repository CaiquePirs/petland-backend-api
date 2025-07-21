package com.petland.modules.sale.service;

import com.petland.common.auth.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.exceptions.InsufficientStockException;
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
    private final AccessValidator accessValidator;

    @Transactional
    public List<ItemsSale> createItemsSale(Sale sale, List<ItemsSaleRequestDTO> itemsSaleRequestDTO) {

        List<ItemsSale> listItemsSale = itemsSaleRequestDTO.stream().map(itemsSaleRequest -> {
            ItemsSale itemsSale = new ItemsSale();
            itemsSale.setCreatedAt(LocalDateTime.now());

            Product product = productService.findById(itemsSaleRequest.productId());

            if (product.getStockQuantity() <= 0 || itemsSaleRequest.productQuantity() > product.getStockQuantity()) {
                throw new InsufficientStockException("Insufficient stock for the product: " + product.getId());
            }

            double totalItemsSale = product.getCostSale().doubleValue() * itemsSaleRequest.productQuantity();

            UUID employee_id = accessValidator.getLoggedInUser();

            itemsSale.setProduct(product);
            itemsSale.setEmployeeAudit(employee_id);

            itemsSale.setItemsSaleTotal(BigDecimal.valueOf(totalItemsSale));
            itemsSale.setProductPrice(product.getCostSale());
            itemsSale.setSale(sale);
            itemsSale.setProductQuantity(itemsSaleRequest.productQuantity());

            int stockUpdated = product.getStockQuantity() - itemsSaleRequest.productQuantity();
            product.setStockQuantity(stockUpdated);

            return itemsSaleRepository.save(itemsSale);
        }).collect(Collectors.toList());
        return listItemsSale;
    }

    public ItemsSale findItemSaleById(UUID saleId, UUID itemSaleId){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        if(sale.getStatus().equals(StatusEntity.DELETED)){
            throw new NotFoundException("Sale ID not found");
        }

       return sale.getItemsSale().stream()
                .filter(itemsSale -> itemsSale.getId().equals(itemSaleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item sale ID not found"));
    }
}
