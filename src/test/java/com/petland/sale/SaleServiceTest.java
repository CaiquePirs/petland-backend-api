package com.petland.sale;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.consultation.model.enums.PaymentType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.sale.builder.SaleFilter;
import com.petland.modules.sale.calculator.SaleCalculator;
import com.petland.modules.sale.controller.dtos.ItemsSaleRequestDTO;
import com.petland.modules.sale.controller.dtos.SaleRequestDTO;
import com.petland.modules.sale.controller.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.repositories.SaleRepository;
import com.petland.modules.sale.service.ItemsSaleService;
import com.petland.modules.sale.service.SaleService;
import com.petland.modules.sale.mappers.SaleMapperGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @Mock private CustomerService customerService;
    @Mock private ProductService productService;
    @Mock private ItemsSaleService itemsSaleService;
    @Mock private AccessValidator access;
    @Mock private SaleRepository repository;
    @Mock private SaleCalculator calculator;
    @Mock private SaleMapperGenerator generate;
    @InjectMocks private SaleService saleService;

    private Customer customer;
    private Employee employee;
    private List<ItemsSale> itemsSales;
    private BigDecimal totalBilled;
    private BigDecimal totalProfit;

    @BeforeEach
    void setUp(){
        itemsSales = List.of(new ItemsSale(), new ItemsSale());
        employee = Employee.builder().build();
        customer = Customer.builder().salesHistory(new ArrayList<>()).build();
        totalBilled = BigDecimal.valueOf(200.00);
        totalProfit = BigDecimal.valueOf(100.00);
    }

    private SaleRequestDTO saleRequest(){
        List<ItemsSaleRequestDTO> itemRequest = List.of(
                ItemsSaleRequestDTO.builder().productId(UUID.randomUUID()).productQuantity(2).build(),
                ItemsSaleRequestDTO.builder().productId(UUID.randomUUID()).productQuantity(2).build()
        );
        return SaleRequestDTO.builder()
                .customerId(UUID.randomUUID())
                .itemsSaleRequestDTO(itemRequest)
                .paymentType(PaymentType.BANK_TRANSFER)
                .build();
    }

    private Sale sale(){
        return Sale.builder()
                .id(UUID.randomUUID())
                .profitSale(BigDecimal.valueOf(200.00))
                .totalSales(BigDecimal.valueOf(100.00))
                .itemsSale(itemsSales)
                .customer(customer)
                .employee(employee)
                .paymentType(PaymentType.BANK_TRANSFER)
                .build();
    }

    @Test
    void shouldRegisterSaleSuccessfully(){
        SaleRequestDTO dto = saleRequest();

        when(customerService.findById(dto.customerId())).thenReturn(customer);
        when(itemsSaleService.createItems(dto.itemsSaleRequestDTO())).thenReturn(itemsSales);
        when(calculator.calculateTotalBilledByItemsSale(itemsSales)).thenReturn(totalBilled);
        when(calculator.calculateProfitByItemsSale(itemsSales)).thenReturn(totalProfit);
        when(access.getEmployeeLogged()).thenReturn(employee);

        Sale saleExpected = sale();
        when(repository.save(any(Sale.class))).thenReturn(saleExpected);

        Sale saleResult = saleService.registerSale(dto);

        assertAll(
                () -> assertEquals(saleExpected.getItemsSale().size(), saleResult.getItemsSale().size()),
                () -> assertEquals(saleExpected.getProfitSale(), saleResult.getProfitSale()),
                () -> assertEquals(saleExpected.getTotalSales(), saleResult.getTotalSales()),
                () -> assertEquals(saleExpected.getCustomer().getId(), saleResult.getCustomer().getId()),
                () -> assertEquals(saleExpected.getEmployee().getId(), saleResult.getEmployee().getId())
        );

        verify(customerService).findById(dto.customerId());
        verify(itemsSaleService).createItems(dto.itemsSaleRequestDTO());
        verify(calculator).calculateProfitByItemsSale(itemsSales);
        verify(calculator).calculateTotalBilledByItemsSale(itemsSales);
        verify(access).getEmployeeLogged();
        verify(repository).save(any(Sale.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound(){
        SaleRequestDTO dto = saleRequest();

        doThrow(new NotFoundException("Customer ID not found")).when(customerService).findById(dto.customerId());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> saleService.registerSale(dto)
        );
        assertEquals("Customer ID not found", ex.getMessage());

        verify(customerService).findById(dto.customerId());
        verify(repository, never()).save(any(Sale.class));
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsNotFound(){
        SaleRequestDTO dto = saleRequest();

        when(customerService.findById(dto.customerId())).thenReturn(customer);
        when(itemsSaleService.createItems(dto.itemsSaleRequestDTO())).thenReturn(itemsSales);
        when(calculator.calculateTotalBilledByItemsSale(itemsSales)).thenReturn(totalBilled);
        when(calculator.calculateProfitByItemsSale(itemsSales)).thenReturn(totalProfit);

        doThrow(new NotFoundException("Employee ID not found")).when(access).getEmployeeLogged();

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                ()-> saleService.registerSale(dto)
        );
        assertEquals("Employee ID not found", ex.getMessage());

        verify(customerService).findById(dto.customerId());
        verify(itemsSaleService).createItems(dto.itemsSaleRequestDTO());
        verify(calculator).calculateProfitByItemsSale(itemsSales);
        verify(calculator).calculateTotalBilledByItemsSale(itemsSales);
        verify(access).getEmployeeLogged();
        verify(repository, never()).save(any(Sale.class));
    }

    @Test
    void shouldReturnSaleWhenFoundById(){
        UUID saleId = UUID.randomUUID();
        Sale sale = sale();
        sale.setStatus(StatusEntity.ACTIVE);

        when(repository.findById(saleId)).thenReturn(Optional.of(sale));
        Sale result = saleService.findSaleById(saleId);

        assertEquals(StatusEntity.ACTIVE, result.getStatus());
        verify(repository).findById(saleId);
    }

    @Test
    void shouldThrowExceptionWhenSaleStatusIsDeleted(){
        UUID saleId = UUID.randomUUID();
        Sale sale = sale();
        sale.setStatus(StatusEntity.DELETED);

        when(repository.findById(saleId)).thenReturn(Optional.of(sale));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> saleService.findSaleById(saleId)
        );
        assertEquals("Sale not found", ex.getMessage());

        verify(repository).findById(saleId);
    }

    @Test
    void shouldThrowExceptionWhenSaleIsNotFound(){
        UUID saleId = UUID.randomUUID();
        when(repository.findById(saleId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> saleService.findSaleById(saleId)
        );
        assertEquals("Sale not found", ex.getMessage());

        verify(repository).findById(saleId);
    }

    @Test
    void testFindAllSalesByFilter_HappyPath() {
        Pageable pageable = PageRequest.of(1, 10);
        SaleFilter filter = new SaleFilter();
        Sale sale = new Sale();
        sale.setId(UUID.randomUUID());
        SaleResponseDTO dto = SaleResponseDTO.builder().id(sale.getId()).build();

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(sale)));
        when(generate.generateSaleResponse(sale)).thenReturn(dto);

        Page<SaleResponseDTO> result = saleService.findAllSalesByFilter(filter, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testFindAllSalesByPeriod_FilterDeleted() {
        Sale sale1 = new Sale();
        sale1.setId(UUID.randomUUID());
        sale1.setStatus(StatusEntity.ACTIVE);

        Sale sale2 = new Sale();
        sale2.setId(UUID.randomUUID());
        sale2.setStatus(StatusEntity.DELETED);

        when(repository.findAll(any(Specification.class))).thenReturn(List.of(sale1, sale2));

        List<Sale> result = saleService.findAllSalesByPeriod(LocalDate.now().minusDays(10), LocalDate.now());

        assertEquals(1, result.size());
        assertEquals(StatusEntity.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void testFindAllSalesByProductId_FilterDeleted() {
        UUID productId = UUID.randomUUID();
        Product product = Product.builder().id(productId).build();

        Sale sale1 =  sale();
        sale1.setStatus(StatusEntity.ACTIVE);

        Sale sale2 = sale();
        sale2.setStatus(StatusEntity.DELETED);

        when(productService.findById(productId)).thenReturn(product);
        when(repository.findAll(any(Specification.class))).thenReturn(List.of(sale1, sale2));

        List<Sale> result = saleService.findAllSalesByProductId(productId);

        assertEquals(1, result.size());
        assertEquals(StatusEntity.ACTIVE, result.get(0).getStatus());
        verify(productService).findById(productId);
    }
}
