package com.petland.product;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.InsufficientStockException;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.product.builder.ProductFilter;
import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.dto.ProductResponseDTO;
import com.petland.modules.product.dto.ProductUpdateDTO;
import com.petland.modules.product.enums.ProductType;
import com.petland.modules.product.mappers.ProductMapper;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.modules.product.service.ProductService;
import com.petland.modules.product.validator.ProductUpdateValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks private ProductService service;
    @Mock private ProductRepository productRepository;
    @Mock private EmployeeService employeeService;
    @Mock private ProductMapper mapper;
    @Mock private ProductUpdateValidator validator;

    private Employee employee;
    private Product product;
    private ProductRequestDTO requestDTO;
    private UUID productId;

    @BeforeEach
    void setUp(){
        productId = UUID.randomUUID();
        product = Product.builder().productType(ProductType.PET_CLOTHING).id(UUID.randomUUID()).build();
        requestDTO = ProductRequestDTO.builder().productType(ProductType.PET_CLOTHING).build();
        employee = Employee.builder().id(UUID.randomUUID()).build();
    }

    @Test
    void shouldCreateProductSuccessfully(){
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenReturn(employee);
        when(mapper.toEntity(requestDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);

        Product result = service.register(employeeId, requestDTO);

        assertAll(
                () -> assertNotNull(result.getBarCode()),
                () -> assertEquals(requestDTO.productType(), result.getProductType())
        );

        verify(employeeService).findById(employeeId);
        verify(mapper).toEntity(requestDTO);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeIsNotFound(){
        UUID employeeId = UUID.randomUUID();
        doThrow(new NotFoundException("Employee ID not found"))
                .when(employeeService).findById(employeeId);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.register(employeeId, requestDTO)
        );
        assertEquals("Employee ID not found", ex.getMessage());

        verify(employeeService).findById(employeeId);
        verify(productRepository, never()).save(product);
    }

    @Test
    void shouldReturnProductWhenFoundById(){
        product.setStatus(StatusEntity.ACTIVE);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Product result = service.findById(productId);

        assertNotNull(result);
        verify(productRepository).findById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductStatusIsDeleted() {
        product.setStatus(StatusEntity.DELETED);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.findById(productId)
        );
        assertEquals("Product not found", ex.getMessage());
        verify(productRepository).findById(productId);
    }

    @Test
    void shouldThrowExceptionWhenProductStatusIsNotFound() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> service.findById(productId)
        );
        assertEquals("Product not found", ex.getMessage());
        verify(productRepository).findById(productId);
    }

    @Test
    void shouldUpdateProductStockSuccessfully() {
        product.setStockQuantity(20);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        assertDoesNotThrow(() -> service.updateStock(5, productId));
        assertEquals(15, product.getStockQuantity());

        verify(productRepository).findById(productId);
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("should throw exception when quantity used is greater than stock product")
    void shouldThrowExceptionWhenQuantityUsedGreaterThanStockProduct() {
        product.setStockQuantity(10);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        InsufficientStockException ex = assertThrows(
                InsufficientStockException.class,
                () -> service.updateStock(15, productId)
        );
        assertEquals("Product stock is insufficient", ex.getMessage());

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(product);
    }

    @Test
    void shouldThrowExceptionWhenProductQuantityIsInsufficient() {
        product.setStockQuantity(0);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        InsufficientStockException ex = assertThrows(
                InsufficientStockException.class,
                () -> service.updateStock(2, productId)
        );
        assertEquals("Product stock is insufficient", ex.getMessage());

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(product);
    }

    @Test
    void shouldDeactivateProductById() {
        product.setId(productId);
        product.setStatus(StatusEntity.ACTIVE);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        service.deactivateById(productId);

        assertEquals(StatusEntity.DELETED, product.getStatus());
        verify(productRepository).save(product);
    }

    @Test
    void shouldReturnPagedProductsByFilter() {
        ProductFilter filter = ProductFilter.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        ProductResponseDTO dto = ProductResponseDTO.builder().build();

        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(Mockito.any(Specification.class), Mockito.eq(pageable))).thenReturn(productPage);
        when(mapper.toDTO(product)).thenReturn(dto);

        Page<ProductResponseDTO> result = service.findAllByFilter(filter, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(dto, result.getContent().get(0));
    }

    @Test
    void shouldUpdateProductById() {
        ProductUpdateDTO dto = ProductUpdateDTO.builder().build();
        Product updatedProduct = new Product();
        ProductResponseDTO responseDTO = ProductResponseDTO.builder().build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        when(validator.validate(dto, product)).thenReturn(updatedProduct);
        when(mapper.toDTO(updatedProduct)).thenReturn(responseDTO);

        ProductResponseDTO result = service.updateById(productId, dto);

        verify(productRepository).save(updatedProduct);
        assertEquals(responseDTO, result);
    }
}
