package com.petland.modules.customer.specifications;

import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerSpecification {

    public static Specification<Customer> filterBy(String name, String email, String phone, StatusEntity status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if (phone != null && !phone.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
            }

            predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
