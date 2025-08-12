package com.petland.modules.petCare.specifications;

import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.criteria.Predicate;

@Component
public class PetCareSpecification {

    public static Specification<PetCare> specification(UUID petId, UUID customerId, UUID employeeId,
                                                       BigDecimal minRevenue, BigDecimal maxCostOperating,
                                                       BigDecimal minProfit, BigDecimal maxProfit,
                                                       LocalDateTime startDate, LocalDateTime endDate) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (petId != null) {
                predicates.add(cb.equal(root.get("pet").get("id"), petId));
            }

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }

            if (employeeId != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), employeeId));
            }

            if (minRevenue != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalRevenue"), minRevenue));
            }

            if (maxCostOperating != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalCostOperating"), maxCostOperating));
            }

            if (minProfit != null && maxProfit != null) {
                predicates.add(cb.between(root.get("totalProfit"), minProfit, maxProfit));

            } else if (minProfit != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalProfit"), minProfit));

            } else if (maxProfit != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalProfit"), maxProfit));
            }

            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("serviceDate"), startDate, endDate));

            } else if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("serviceDate"), startDate));

            } else if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("serviceDate"), endDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public static Specification<PetCare> reportSpecification(LocalDate dateMin, LocalDate dateMax){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dateMin != null && dateMax != null) {
                predicates.add(cb.between(root.get("serviceDate"), dateMin, dateMax));

            } else if (dateMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("serviceDate"), dateMin));

            } else if (dateMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("serviceDate"), dateMax));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }

    public static Specification<PetCare> findByServiceType(PetCareType petCareType) {
        return (root, query, cb) -> {
            Join<PetCare, PetCareDetails> detailsJoin = root.join("petCareDetails");
            return cb.equal(detailsJoin.get("petCareType"), petCareType);
        };
    }
}
