package com.petland.modules.product.validator;

import com.petland.modules.product.dto.ProductUpdateDTO;
import com.petland.modules.product.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductUpdateValidator {

    public Product validate(ProductUpdateDTO dto, Product product) {
        if (dto.name() != null && !dto.name().isBlank()) {
            product.setName(dto.name());
        }
        if (dto.description() != null && !dto.description().isBlank()) {
            product.setDescription(dto.description());
        }
        if (dto.brand() != null && !dto.brand().isBlank()) {
            product.setBrand(dto.brand());
        }
        if (dto.supplierName() != null && !dto.supplierName().isBlank()) {
            product.setSupplierName(dto.supplierName());
        }
        if (dto.productType() != null) {
            product.setProductType(dto.productType());
        }
        if (dto.manufactureDate() != null) {
            product.setManufactureDate(dto.manufactureDate());
        }
        if (dto.expirationDate() != null) {
            product.setExpirationDate(dto.expirationDate());
        }
        if (dto.costPrice() != null) {
            product.setCostPrice(dto.costPrice());
        }
        if (dto.costSale() != null) {
            product.setCostSale(dto.costSale());
        }
        if (dto.stockQuantity() != null) {
            product.setStockQuantity(product.getStockQuantity() + dto.stockQuantity());
        }
        return product;
    }

}
