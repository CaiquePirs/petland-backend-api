package com.petland.modules.pet.specifications;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.pet.model.Pet;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

@Component
public class PetSpecification {

    public static Specification<Pet> filterBy(String name,  String specie,
                                       String gender, String breed, StatusEntity status){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if(breed != null && !breed.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("breed")), "%" + breed.toLowerCase() + "%"));
            }

            if(specie != null && !specie.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("specie")), "%" + specie.toLowerCase() + "%"));
            }

            if(gender != null && !gender.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("gender")), "%" + gender.toLowerCase() + "%"));
            }

            predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
