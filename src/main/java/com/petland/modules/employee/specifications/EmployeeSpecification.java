package com.petland.modules.employee.specifications;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.employee.builder.EmployeeFilter;
import com.petland.modules.employee.model.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeSpecification {

    public static Specification<Employee> Specification(EmployeeFilter filter){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getName() != null && !filter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
            }
            if (filter.getPhone() != null && !filter.getPhone().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + filter.getPhone().toLowerCase() + "%"));
            }
            if (filter.getDepartment() != null && !filter.getDepartment().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("department")), "%" + filter.getDepartment().toLowerCase() + "%"));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }

}
