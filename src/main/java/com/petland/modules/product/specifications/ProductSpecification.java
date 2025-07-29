package com.petland.modules.product.specifications;

import com.petland.enums.StatusEntity;
import com.petland.modules.product.enums.ProductType;
import com.petland.modules.product.model.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> specification(String name, String brand, ProductType productType,
                                                       LocalDate manufactureDateMin, LocalDate expirationDateMax,
                                                       BigDecimal costSaleMin, Integer stockMin, StatusEntity status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (brand != null && !brand.isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
            }

            if (productType != null) {
                predicates.add(cb.equal(root.get("productType"), productType));
            }

            if (manufactureDateMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("manufactureDate"), manufactureDateMin));
            }

            if (expirationDateMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expirationDate"), expirationDateMax));
            }

            if (costSaleMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("costSale"), costSaleMin));
            }

            if (stockMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stockQuantity"), stockMin));
            }
            predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

