package com.petland.modules.product.specifications;

import com.petland.modules.product.builder.ProductFilter;
import com.petland.modules.product.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> specification(ProductFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getBrand() != null && !filter.getBrand().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), filter.getBrand().toLowerCase()));
            }
            if (filter.getProductType() != null) {
                predicates.add(cb.equal(root.get("productType"), filter.getProductType()));
            }
            if (filter.getManufactureDateMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("manufactureDate"), filter.getManufactureDateMin()));
            }
            if (filter.getExpirationDateMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expirationDate"), filter.getExpirationDateMax()));
            }
            if (filter.getCostSaleMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("costSale"), filter.getCostSaleMin()));
            }
            if (filter.getStockMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stockQuantity"), filter.getStockMin()));
            }
            if(filter.getStatus() != null){
                predicates.add(cb.equal(cb.lower(root.get("status")), filter.getStatus()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

