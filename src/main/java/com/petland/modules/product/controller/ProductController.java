package com.petland.modules.product.controller;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.dto.ProductResponseDTO;
import com.petland.modules.product.dto.ProductUpdateDTO;
import com.petland.modules.product.enums.ProductType;
import com.petland.modules.product.mappers.ProductMapper;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final HttpServletRequest request;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> register(@RequestBody @Valid ProductRequestDTO productRequestDTO){
        String employeeId = request.getAttribute("id").toString();
        Product product = productService.register(UUID.fromString(employeeId), productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDTO(product));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> findProductById(@PathVariable(name = "id") UUID productId){
        Product product = productService.findById(productId);
        return ResponseEntity.ok(productMapper.toDTO(product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateProductById(@PathVariable(name = "id") UUID productId){
        productService.deactivateById(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ProductResponseDTO>> findAllProductsByFilter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) ProductType productType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate manufactureDateMin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDateMax,
            @RequestParam(required = false) BigDecimal costSaleMin,
            @RequestParam(required = false) Integer stockMin,
            @RequestParam(required = false, defaultValue = "ACTIVE") StatusEntity status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        Page<ProductResponseDTO> productList = productService.findAllByFilter(
                name, brand, productType, manufactureDateMin,
                expirationDateMax, costSaleMin, stockMin,  status, PageRequest.of(page, size)
        );

        return ResponseEntity.ok(productList);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProductById(@PathVariable(name = "id") UUID productId,
                                                            @RequestBody ProductUpdateDTO productDTO){
        ProductResponseDTO productUpdated = productService.updateById(productId, productDTO);
        return ResponseEntity.ok(productUpdated);
    }

}
