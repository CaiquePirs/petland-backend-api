package com.petland.modules.vaccination.specifications;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.vaccination.enums.VaccineType;
import com.petland.modules.vaccination.module.Vaccine;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VaccineSpecification {

    public static Specification<Vaccine> specification(String lotNumber, String supplierName, VaccineType vaccineType, BigDecimal minPurchasePrice,
                                                            BigDecimal maxPurchasePrice, BigDecimal minPriceSale, BigDecimal maxPriceSale,
                                                            Integer minStockQuantity, Integer maxStockQuantity, LocalDate manufactureAfter,
                                                            LocalDate expirationBefore, StatusEntity status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (lotNumber != null && !lotNumber.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("lotNumber")), "%" + lotNumber.toLowerCase() + "%"));
            }

            if (supplierName != null && !supplierName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("supplierName")), "%" + supplierName.toLowerCase() + "%"));
            }

            if (vaccineType != null) {
                predicates.add(cb.equal(root.get("vaccineType"), vaccineType));
            }

            if (minPurchasePrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("purchasePrice"), minPurchasePrice));
            }

            if (maxPurchasePrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("purchasePrice"), maxPurchasePrice));
            }

            if (minPriceSale != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("priceSale"), minPriceSale));
            }

            if (maxPriceSale != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("priceSale"), maxPriceSale));
            }

            if (minStockQuantity != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stockQuantity"), minStockQuantity));
            }

            if (maxStockQuantity != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("stockQuantity"), maxStockQuantity));
            }

            if (manufactureAfter != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("manufactureDate"), manufactureAfter));
            }

            if (expirationBefore != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expirationDate"), expirationBefore));
            }

            predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

