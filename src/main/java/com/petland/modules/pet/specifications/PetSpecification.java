package com.petland.modules.pet.specifications;

import com.petland.modules.pet.builder.PetFilter;
import com.petland.modules.pet.model.Pet;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@Component
public class PetSpecification {

    public static Specification<Pet> filterBy(PetFilter filter){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getBreed() != null && !filter.getBreed().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("breed")), "%" + filter.getBreed().toLowerCase() + "%"));
            }
            if (filter.getSpecie() != null) {
                predicates.add(cb.equal(root.get("specie"), filter.getSpecie()));
            }

            if (filter.getGender() != null) {
                predicates.add(cb.equal(root.get("gender"), filter.getGender()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            predicates.add(cb.equal(root.get("status"), filter.getStatus() != null ? filter.getStatus() : "ACTIVE"));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
