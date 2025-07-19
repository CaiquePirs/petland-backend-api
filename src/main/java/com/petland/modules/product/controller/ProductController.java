package com.petland.modules.product.controller;

import com.petland.modules.product.dto.ProductRequestDTO;
import com.petland.modules.product.dto.ProductResponseDTO;
import com.petland.modules.product.mappers.ProductMapper;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok().body(productMapper.toDTO(product));
    }
}
