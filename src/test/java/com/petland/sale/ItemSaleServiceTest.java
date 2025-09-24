package com.petland.sale;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.InsufficientStockException;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.calculator.SaleCalculator;
import com.petland.modules.sale.controller.dtos.ItemsSaleRequestDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.ItemsSaleRepository;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.modules.sale.service.ItemsSaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemSaleServiceTest {

    @Mock private ProductService productService;
    @Mock private ItemsSaleRepository itemsSaleRepository;
    @Mock private SaleRepository saleRepository;
    @Mock private SaleCalculator saleCalculator;
    @InjectMocks private ItemsSaleService itemsSaleService;

    private UUID productId;
    private Product product;
    private ItemsSale item1;
    private ItemsSale item2;

    @BeforeEach
    void seUp(){
        productId = UUID.randomUUID();
        product = Product.builder().id(productId).costSale(BigDecimal.valueOf(30.00)).build();
        item1 = new ItemsSale();
        item1.setId(UUID.randomUUID());
        item1.setStatus(StatusEntity.ACTIVE);

        item2 = new ItemsSale();
        item2.setId(UUID.randomUUID());
        item2.setStatus(StatusEntity.ACTIVE);
    }

    @Test
    void shouldCreateItemSaleSuccessfully(){
        ItemsSaleRequestDTO dto = ItemsSaleRequestDTO.builder().productQuantity(2).productId(productId).build();

        when(productService.findById(dto.productId())).thenReturn(product);
        doNothing().when(productService).updateStock(dto.productQuantity(), product.getId());

        when(saleCalculator.calculateTotalSale(dto.productQuantity(),
                product.getCostSale())).thenReturn(BigDecimal.valueOf(30.00)
        );
        when(saleCalculator.calculateProfitByItemSale(product,
                dto.productQuantity())).thenReturn(BigDecimal.valueOf(15.00)
        );

        List<ItemsSale> result = itemsSaleService.createItems(List.of(dto));
        assertEquals(1, result.size());

        assertAll(
                () -> assertEquals(product, result.get(0).getProduct()),
                () -> assertEquals(BigDecimal.valueOf(30.00), result.get(0).getItemsSaleTotal()),
                () -> assertEquals(BigDecimal.valueOf(15.00), result.get(0).getProfit())
        );

        verify(productService).findById(dto.productId());
        verify(productService).updateStock(dto.productQuantity(), product.getId());
        verify(saleCalculator).calculateProfitByItemSale(product, dto.productQuantity());
        verify(saleCalculator).calculateTotalSale(dto.productQuantity(), product.getCostSale());
    }

    @Test
    void shouldThrowExceptionWhenProductIsNotFound() {
        ItemsSaleRequestDTO dto = ItemsSaleRequestDTO.builder().productId(productId).build();
        doThrow(new NotFoundException("Product ID not found")).when(productService).findById(dto.productId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> itemsSaleService.createItems(List.of(dto))
        );
        assertEquals("Product ID not found", ex.getMessage());
        verify(productService).findById(dto.productId());
    }

    @Test
    @DisplayName("Should throw exception when product stock quantity is insufficient")
    void shouldThrowExceptionWhenStockIsInsufficient(){
        product.setStockQuantity(5);

        ItemsSaleRequestDTO dto = ItemsSaleRequestDTO.builder().productQuantity(10).productId(productId).build();
        when(productService.findById(dto.productId())).thenReturn(product);

        doThrow(new InsufficientStockException("Product stock is insufficient"))
                .when(productService).updateStock(dto.productQuantity(), product.getId());

        InsufficientStockException ex = assertThrows(
                InsufficientStockException.class,
                ()-> itemsSaleService.createItems(List.of(dto))
        );
        assertEquals("Product stock is insufficient", ex.getMessage());
    }


    @Test
    void shouldReturnItemWhenSaleAndItemAreActive() {
        UUID saleId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        ItemsSale item = new ItemsSale();
        item.setId(itemId);
        item.setStatus(StatusEntity.ACTIVE);

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setStatus(StatusEntity.ACTIVE);
        sale.setItemsSale(List.of(item));

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        ItemsSale result = itemsSaleService.findActiveItemInActiveSale(saleId, itemId);

        assertEquals(itemId, result.getId());
        assertEquals(StatusEntity.ACTIVE, result.getStatus());
    }

    @Test
    void shouldThrowWhenSaleNotFound() {
        UUID saleId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemsSaleService.findActiveItemInActiveSale(saleId, itemId)
        );
        assertEquals("Sale not found", ex.getMessage());
    }

    @Test
    void shouldThrowWhenSaleIsDeleted() {
        UUID saleId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setStatus(StatusEntity.DELETED);

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> itemsSaleService.findActiveItemInActiveSale(saleId, itemId)
        );
        assertEquals("Sale not found", ex.getMessage());
    }

    @Test
    void shouldThrowWhenItemNotFoundInSale() {
        UUID saleId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setStatus(StatusEntity.ACTIVE);
        sale.setItemsSale(List.of());

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> itemsSaleService.findActiveItemInActiveSale(saleId, itemId)
        );
        assertTrue(ex.getMessage().contains("ItemSale with ID " + itemId));
    }

    @Test
    void shouldThrowWhenItemIsDeleted() {
        UUID saleId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        ItemsSale item = new ItemsSale();
        item.setId(itemId);
        item.setStatus(StatusEntity.DELETED);

        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setStatus(StatusEntity.ACTIVE);
        sale.setItemsSale(List.of(item));

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> itemsSaleService.findActiveItemInActiveSale(saleId, itemId)
        );
        assertTrue(ex.getMessage().contains("ItemSale with ID " + itemId));
    }

    @Test
    void testDeactivateItemsList_HappyPath() {
        List<ItemsSale> items = List.of(item1, item2);

        itemsSaleService.deactivateItemsList(items);

        assertEquals(StatusEntity.DELETED, item1.getStatus());
        assertEquals(StatusEntity.DELETED, item2.getStatus());

        verify(itemsSaleRepository).saveAll(items);
    }

    @Test
    void testDeactivateItemsList_EmptyList() {
        itemsSaleService.deactivateItemsList(List.of());
        verify(itemsSaleRepository, never()).saveAll(any());
    }

    @Test
    void testDeactivateActiveItemInActiveSale_HappyPath() {
        UUID saleId = UUID.randomUUID();
        UUID itemId = item1.getId();

        ItemsSaleService spyService = spy(itemsSaleService);
        doReturn(item1).when(spyService).findActiveItemInActiveSale(saleId, itemId);

        spyService.deactivateActiveItemInActiveSale(saleId, itemId);

        assertEquals(StatusEntity.DELETED, item1.getStatus());
        verify(itemsSaleRepository).save(item1);
    }

    @Test
    void testDeactivateActiveItemInActiveSale_ItemNotFound() {
        UUID saleId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        ItemsSaleService spyService = spy(itemsSaleService);
            doThrow(new NotFoundException("Item not found")).when(spyService).findActiveItemInActiveSale(saleId, itemId);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> spyService.deactivateActiveItemInActiveSale(saleId, itemId)
        );

        assertEquals("Item not found", ex.getMessage());
        verify(itemsSaleRepository, never()).save(any());
    }

}
