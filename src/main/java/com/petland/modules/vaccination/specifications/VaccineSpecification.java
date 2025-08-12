package com.petland.modules.vaccination.specifications;

import com.petland.modules.vaccination.builder.VaccineFilter;
import com.petland.modules.vaccination.module.Vaccine;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VaccineSpecification {

    public static Specification<Vaccine> specification(VaccineFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getLotNumber() != null && !filter.getLotNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("lotNumber")), "%" + filter.getLotNumber().toLowerCase() + "%"));
            }
            if (filter.getSupplierName() != null && !filter.getSupplierName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("supplierName")), "%" + filter.getSupplierName().toLowerCase() + "%"));
            }
            if (filter.getVaccineType() != null) {
                predicates.add(cb.equal(root.get("vaccineType"), filter.getVaccineType()));
            }
            if (filter.getMinPurchasePrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("purchasePrice"), filter.getMinPurchasePrice()));
            }
            if (filter.getMaxPurchasePrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("purchasePrice"), filter.getMaxPurchasePrice()));
            }
            if (filter.getMinPriceSale() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("priceSale"), filter.getMinPriceSale()));
            }
            if (filter.getMaxPriceSale() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("priceSale"), filter.getMaxPriceSale()));
            }
            if (filter.getMinStockQuantity() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stockQuantity"), filter.getMinStockQuantity()));
            }
            if (filter.getMaxStockQuantity() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("stockQuantity"), filter.getMaxStockQuantity()));
            }
            if (filter.getManufactureAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("manufactureDate"), filter.getManufactureAfter()));
            }
            if (filter.getExpirationBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("expirationDate"), filter.getExpirationBefore()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

