package com.petland.modules.petCare.specifications;

import com.petland.modules.petCare.builder.PetCareFilter;
import com.petland.modules.petCare.model.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

@Component
public class PetCareSpecification {

    public static Specification<PetCare> specification(PetCareFilter filter) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getPetId() != null) {
                predicates.add(cb.equal(root.get("pet").get("id"), filter.getPetId()));
            }
            if (filter.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), filter.getCustomerId()));
            }
            if (filter.getEmployeeId() != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), filter.getEmployeeId()));
            }
            if (filter.getMinRevenue() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalRevenue"), filter.getMinRevenue()));
            }
            if (filter.getMaxCostOperating() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalCostOperating"), filter.getMaxCostOperating()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            if (filter.getMinProfit() != null && filter.getMaxProfit() != null) {
                predicates.add(cb.between(root.get("totalProfit"), filter.getMinProfit(), filter.getMaxProfit()));
            } else if (filter.getMinProfit() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalProfit"), filter.getMinProfit()));
            } else if (filter.getMaxProfit() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalProfit"), filter.getMaxProfit()));
            }

            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                predicates.add(cb.between(root.get("serviceDate"), filter.getStartDate(), filter.getEndDate()));
            } else if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("serviceDate"), filter.getStartDate()));
            } else if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("serviceDate"), filter.getEndDate()));
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
