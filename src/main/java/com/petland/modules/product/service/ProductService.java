package com.petland.modules.product.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.dto.ProductResponseDTO;
import com.petland.modules.product.enums.ProductType;
import com.petland.modules.product.mappers.ProductMapper;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.common.exception.InsufficientStockException;
import com.petland.modules.product.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final EmployeeService employeeService;
    private final ProductMapper mapper;

    public Product register(UUID employeeId, ProductRequestDTO productRequestDTO){
        Employee employee = employeeService.findById(employeeId);
        Product product = productMapper.toEntity(productRequestDTO);
        product.setBarCode(UUID.randomUUID());
        product.setEmployee(employee);
        return productRepository.save(product);
    }


    public Product findById(UUID productId){
        return productRepository.findById(productId)
                .filter(p -> !p.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    public void updateProductStock(int stockUsed, UUID productId){
        Product product = findById(productId);

        if(product.getStockQuantity() <= 0 || stockUsed > product.getStockQuantity()){
            throw new InsufficientStockException("Vaccine stock is insufficient");
        }

        int stockUpdated = product.getStockQuantity() - stockUsed;
        product.setStockQuantity(stockUpdated);
        productRepository.save(product);
    }

    public void deleteById(UUID productId){
        Product product = findById(productId);
        product.setStatus(StatusEntity.DELETED);
        productRepository.save(product);
    }

    public Page<ProductResponseDTO> findAllByFilter(String name, String brand, ProductType productType,
                                         LocalDate manufactureDateMin, LocalDate expirationDateMax,
                                         BigDecimal costSaleMin, Integer stockMin, StatusEntity status, Pageable pageable){

        List<ProductResponseDTO> productList = productRepository.findAll(ProductSpecification.specification(
                name, brand, productType, manufactureDateMin, expirationDateMax,
                costSaleMin, stockMin, status), pageable)
                .get()
                .map(mapper::toDTO)
                .toList();

        return new PageImpl<>(productList, pageable, productList.size());
    }

}
