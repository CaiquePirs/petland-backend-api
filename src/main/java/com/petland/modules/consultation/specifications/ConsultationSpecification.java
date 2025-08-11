package com.petland.modules.consultation.specifications;

import com.petland.modules.consultation.builder.ConsultationFilter;
import com.petland.modules.consultation.model.Consultation;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ConsultationSpecification {

    public static Specification<Consultation> filter(ConsultationFilter filter) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), filter.getCustomerId()));
            }
            if (filter.getEmployeeId() != null) {
                predicates.add(cb.equal(root.get("employee").get("id"), filter.getEmployeeId()));
            }
            if (filter.getSaleId() != null) {
                predicates.add(cb.equal(root.get("sales").get("id"), filter.getSaleId()));
            }
            if (filter.getVaccinationId() != null) {
                predicates.add(cb.equal(root.get("vaccination").get("id"), filter.getVaccinationId()));
            }
            if (filter.getServiceId() != null) {
                predicates.add(cb.equal(root.get("service").get("id"), filter.getServiceId()));
            }
            if (filter.getConsultationStatus() != null) {
                predicates.add(cb.equal(root.get("consultationStatus"), filter.getConsultationStatus()));
            }
            if (filter.getType() != null) {
                predicates.add(cb.equal(root.get("type"), filter.getType()));
            }
            if (filter.getPriority() != null) {
                predicates.add(cb.equal(root.get("priority"), filter.getPriority()));
            }
            if (filter.getPaymentType() != null) {
                predicates.add(cb.equal(root.get("paymentType"), filter.getPaymentType()));
            }
            if (filter.getServiceDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("serviceDate"), filter.getServiceDateFrom()));
            }
            if (filter.getServiceDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("serviceDate"), filter.getServiceDateTo()));
            }
            if (filter.getTotalByService() != null) {
                predicates.add(cb.equal(root.get("totalByService"), filter.getTotalByService()));
            }
            if (filter.getProfitByService() != null) {
                predicates.add(cb.equal(root.get("profitByService"), filter.getProfitByService()));
            }
            if (filter.getCostOperatingByService() != null) {
                predicates.add(cb.equal(root.get("costOperatingByService"), filter.getCostOperatingByService()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
