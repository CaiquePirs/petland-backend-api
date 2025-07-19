package com.petland.modules.product.service;

import com.petland.common.auth.AccessValidator;
import com.petland.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.mappers.ProductMapper;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final EmployeeService employeeService;
    private final AccessValidator accessValidator;

    public Product register(UUID employeeId, ProductRequestDTO productRequestDTO){
        Employee employee = employeeService.findById(employeeId);

        Product product = productMapper.toEntity(productRequestDTO);
        product.setBarCode(UUID.randomUUID());
        product.setStatus(StatusEntity.ACTIVE);

        UUID employerLogged = accessValidator.getLoggedInUser();
        product.setEmployee_audit(employerLogged);

        product.setEmployee(employee);
        return productRepository.save(product);
    }
}
