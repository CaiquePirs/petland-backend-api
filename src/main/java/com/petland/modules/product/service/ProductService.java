package com.petland.modules.product.service;

import com.petland.common.auth.AccessValidator;
import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.mappers.ProductMapper;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import com.petland.modules.sale.exceptions.InsufficientStockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final EmployeeService employeeService;

    public Product register(UUID employeeId, ProductRequestDTO productRequestDTO){
        Employee employee = employeeService.findById(employeeId);

        Product product = productMapper.toEntity(productRequestDTO);
        product.setBarCode(UUID.randomUUID());
        product.setStatus(StatusEntity.ACTIVE);

        product.setEmployee(employee);
        return productRepository.save(product);
    }


    public Product findById(UUID productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if(product.getStatus().equals(StatusEntity.DELETED)){
            throw new NotFoundException("Product not found");
        }

        return product;
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



}
