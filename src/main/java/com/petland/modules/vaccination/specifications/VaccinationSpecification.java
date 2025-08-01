package com.petland.modules.vaccination.specifications;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.vaccination.module.Vaccination;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class VaccinationSpecification {
    public static Specification<Vaccination> specifications(UUID petId, UUID customerId, UUID veterinarianId,
                                                     LocalDate vaccinationDate, LocalDate nextDoseBefore, StatusEntity status){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (petId != null) {
                predicates.add(cb.equal(root.get("pet"), petId));
            }

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer"), customerId));
            }

            if (veterinarianId != null) {
                predicates.add(cb.equal(root.get("veterinarian"), veterinarianId));
            }

            if (vaccinationDate != null) {
                predicates.add(cb.equal(root.get("vaccinationDate"), vaccinationDate));
            }

            if (nextDoseBefore != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("nextDoseDate"), nextDoseBefore));
            }
            predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Vaccination> findByPeriod(LocalDate dateMin, LocalDate dateMax){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(dateMin != null || dateMax !=null){
                predicates.add(cb.between(root.get("vaccinationDate"), dateMin, dateMax));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
