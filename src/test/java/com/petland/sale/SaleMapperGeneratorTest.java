package com.petland.sale;

import com.petland.modules.consultation.model.enums.PaymentType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.product.model.Product;
import com.petland.modules.sale.controller.dtos.ItemsSaleResponseDTO;
import com.petland.modules.sale.controller.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.mappers.SaleMapperGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SaleMapperGeneratorTest {

    private final SaleMapperGenerator saleMapperGenerator = new SaleMapperGenerator();

    @Test
    void shouldGenerateSaleResponseWithAllValues() {
        UUID saleId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Sale sale = Sale.builder().id(saleId).customer(Customer.builder().id(customerId).build())
                .employee(Employee.builder().id(employeeId).build())
                .totalSales(BigDecimal.valueOf(100))
                .paymentType(PaymentType.BANK_TRANSFER).build();

        ItemsSale item = ItemsSale.builder().sale(sale)
                .id(itemId).product(Product.builder().id(productId).build()).productQuantity(2)
                .itemsSaleTotal(BigDecimal.valueOf(50)).build();

        sale.setItemsSale(List.of(item));
        SaleResponseDTO response = saleMapperGenerator.generateSaleResponse(sale);

        assertNotNull(response);
        assertEquals(saleId, response.getId());
        assertEquals(customerId, response.getCustomerId());
        assertEquals(employeeId, response.getEmployeeId());
        assertEquals(sale.getPaymentType(), response.getPaymentType());
        assertEquals(BigDecimal.valueOf(100), response.getTotalSales());

        assertNotNull(response.getItemsSaleResponseDTO());
        assertEquals(1, response.getItemsSaleResponseDTO().size());

        ItemsSaleResponseDTO itemResponse = response.getItemsSaleResponseDTO().get(0);
        assertEquals(itemId, itemResponse.getId());
        assertEquals(saleId, itemResponse.getSaleId());
        assertEquals(productId, itemResponse.getProductId());
        assertEquals(2, itemResponse.getProductQuantity());
        assertEquals(BigDecimal.valueOf(50), itemResponse.getItemsSaleTotal());
    }

    @Test
    void shouldGenerateEmptyItemsResponseWhenSaleHasNoItems() {
        // given
        UUID saleId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();

        Customer customer = new Customer();
        customer.setId(customerId);

        Employee employee = new Employee();
        employee.setId(employeeId);

        Sale sale = Sale.builder().id(saleId).customer(Customer.builder().id(customerId).build())
                .employee(Employee.builder().id(employeeId).build()).paymentType(PaymentType.CREDIT_CARD)
                .totalSales(BigDecimal.ZERO).itemsSale(List.of())
                .build();

        SaleResponseDTO response = saleMapperGenerator.generateSaleResponse(sale);

        assertNotNull(response);
        assertEquals(saleId, response.getId());
        assertEquals(customerId, response.getCustomerId());
        assertEquals(employeeId, response.getEmployeeId());
        assertEquals(PaymentType.CREDIT_CARD, response.getPaymentType());
        assertEquals(BigDecimal.ZERO, response.getTotalSales());

        assertNotNull(response.getItemsSaleResponseDTO());
        assertTrue(response.getItemsSaleResponseDTO().isEmpty());
    }
}

