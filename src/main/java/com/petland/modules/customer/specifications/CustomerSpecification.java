package com.petland.modules.customer.specifications;

import com.petland.modules.customer.builder.CustomerFilter;
import com.petland.modules.customer.model.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerSpecification {

    public static Specification<Customer> filterBy(CustomerFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail() .isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail() .toLowerCase() + "%"));
            }
            if (filter.getPhone() != null && !filter.getPhone().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + filter.getPhone().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
