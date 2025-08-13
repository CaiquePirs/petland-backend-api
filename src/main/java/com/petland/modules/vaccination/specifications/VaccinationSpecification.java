package com.petland.modules.vaccination.specifications;

import com.petland.modules.vaccination.builder.VaccinationFilter;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class VaccinationSpecification {
    public static Specification<Vaccination> specifications(VaccinationFilter filter){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getPetId() != null) {
                predicates.add(cb.equal(root.get("pet").get("id"), filter.getPetId()));
            }
            if (filter.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), filter.getCustomerId()));
            }
            if (filter.getVeterinarianId() != null) {
                predicates.add(cb.equal(root.get("veterinarian").get("id"), filter.getVeterinarianId()));
            }
            if (filter.getVaccinationDate() != null) {
                predicates.add(cb.equal(root.get("vaccinationDate"), filter.getVaccinationDate()));
            }
            if (filter.getNextDoseBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("nextDoseDate"), filter.getNextDoseBefore()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
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

    public static Specification<Vaccination> findByVaccine(Vaccine vaccine){
        return (root, query, cb) -> {
            Join<Vaccination, AppliedVaccine> join = root.join("appliedVaccines");
            return cb.equal(join.get("vaccine").get("id"), vaccine.getId());
        };
    }
}
