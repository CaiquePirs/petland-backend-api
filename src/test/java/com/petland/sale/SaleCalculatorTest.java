package com.petland.sale;

import com.petland.modules.product.model.Product;
import com.petland.modules.sale.calculator.SaleCalculator;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SaleCalculatorTest {

    private SaleCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SaleCalculator();
    }

    private ItemsSale itemsSale(){
        return ItemsSale.builder()
                .itemsSaleTotal(BigDecimal.valueOf(100.00))
                .profit(BigDecimal.valueOf(50.00))
                .productQuantity(2)
                .product(Product.builder().costPrice(BigDecimal.valueOf(10.00)).build())
                .build();
    }

    private Sale sale(){
        ItemsSale item1 = itemsSale();
        ItemsSale item2 = itemsSale();

        return Sale.builder()
                .totalSales(BigDecimal.valueOf(200.00))
                .profitSale(BigDecimal.valueOf(100.00))
                .itemsSale(List.of(item1, item2))
                .build();
    }

    @Test
    void testCalculateTotalSale_HappyPath() {
        BigDecimal result = calculator.calculateTotalSale(5, new BigDecimal("10.00"));
        assertEquals(new BigDecimal("50.00"), result);
    }

    @Test
    void testCalculateTotalSale_Error_InvalidQuantity() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> calculator.calculateTotalSale(-1, new BigDecimal("10.00"))
        );
        assertEquals("Invalid values for calculation.", exception.getMessage());
    }

    @Test
    void testCalculateTotalSale_Error_NullValue() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> calculator.calculateTotalSale(5, null)
        );
        assertEquals("Invalid values for calculation.", exception.getMessage());
    }

    @Test
    void testCalculateTotalSale_Error_NegativeValue() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> calculator.calculateTotalSale(5, new BigDecimal("-10.00"))
        );
        assertEquals("Invalid values for calculation.", exception.getMessage());
    }

    @Test
    void testCalculateProfitByItemSale_HappyPath() {
        Product product = itemsSale().getProduct();
        product.setCostSale(BigDecimal.valueOf(15.00));
        product.setCostPrice(BigDecimal.valueOf(10.00));

        BigDecimal result = calculator.calculateProfitByItemSale(product, 3);
        assertEquals(BigDecimal.valueOf(15.00), result);
    }

    @Test
    void testCalculateProfitByItemSale_Error_NullProduct() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> calculator.calculateProfitByItemSale(null, 2)
        );
        assertEquals("Error performing calculation", exception.getMessage());
    }

    @Test
    void testCalculateProfitByItemSale_Error_ProductWithNullValues() {
        Product product = itemsSale().getProduct();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> calculator.calculateProfitByItemSale(product, 2)
        );
        assertEquals("Error performing calculation", exception.getMessage());
    }

    @Test
    void shouldCalculateTotalBilledByItemSuccessfully(){
        List<ItemsSale> itemsSales = List.of(itemsSale(), itemsSale(), itemsSale());
        BigDecimal totalBilled = calculator.calculateTotalBilledByItemsSale(itemsSales);
        assertEquals(BigDecimal.valueOf(300.00), totalBilled);
    }

    @Test
    void shouldCalculateTotalBilledListSalesSuccessfully(){
        List<Sale> sales = List.of(sale(), sale());
        BigDecimal totalBilled = calculator.calculateTotalBilledBySaleList(sales);
        assertEquals(BigDecimal.valueOf(400.00), totalBilled);
    }

    @Test
    void shouldSumTotalItemsSaleSuccessfully(){
        List<Sale> sales = List.of(sale(), sale(), sale());
        Integer totalItems = calculator.sumQuantityItemsSale(sales);
        assertEquals(6, totalItems);
    }

    @Test
    void shouldCalculateTotalProfitByItemsSuccessfully(){
        List<ItemsSale> itemsSales = List.of(itemsSale(), itemsSale(), itemsSale());
        BigDecimal totalProfit = calculator.calculateProfitByItemsSale(itemsSales);
        assertEquals(BigDecimal.valueOf(150.00), totalProfit);
    }

    @Test
    void shouldCalculateTotalProfitBySalesSuccessfully(){
        List<Sale> sales = List.of(sale(), sale(), sale());
        BigDecimal totalProfit = calculator.calculateProfitBySales(sales);
        assertEquals(BigDecimal.valueOf(300.00), totalProfit);
    }

    @Test
    void shouldCalculateTotalCostProductsSuccessfully(){
        List<Sale> sales = List.of(sale(), sale(), sale());
        BigDecimal totalCost = calculator.calculateTotalCostProducts(sales);
        assertEquals(BigDecimal.valueOf(120.00), totalCost);
    }
}
