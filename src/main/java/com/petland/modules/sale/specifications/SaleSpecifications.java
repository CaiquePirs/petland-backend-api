package com.petland.modules.sale.specifications;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.sale.model.Sale;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SaleSpecifications {

    public static Specification<Sale> specifications(UUID employeeId, UUID customerId,
                                                     PaymentType paymentType, BigDecimal totalSalesMin,
                                                     BigDecimal totalSalesMax, StatusEntity status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (employeeId != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), employeeId));
            }

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }

            if (paymentType != null) {
                predicates.add(cb.equal(root.get("paymentType"), paymentType));
            }

            if (totalSalesMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalSales"), totalSalesMin));
            }

            if (totalSalesMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalSales"), totalSalesMax));
            }

            predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Sale> findByPeriod(LocalDate dateMin, LocalDate dateMax) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dateMin != null && dateMax != null) {
                predicates.add(cb.between(root.get("createAt"), dateMin, dateMax));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
