package com.petland.modules.product.controller;

import com.petland.modules.product.builder.ProductFilter;
import com.petland.modules.product.controller.doc.ProductApi;
import com.petland.modules.product.controller.dto.ProductRequestDTO;
import com.petland.modules.product.controller.dto.ProductResponseDTO;
import com.petland.modules.product.controller.dto.ProductUpdateDTO;
import com.petland.modules.product.mappers.ProductMapper;
import com.petland.modules.product.model.Product;
import com.petland.modules.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController implements ProductApi {

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
            @ModelAttribute ProductFilter filter,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        Page<ProductResponseDTO> productList = productService.findAllByFilter(
                filter, PageRequest.of(page, size)
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
