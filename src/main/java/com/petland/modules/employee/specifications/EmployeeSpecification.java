package com.petland.modules.employee.specifications;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeSpecification {

    public static Specification<Employee> Specification(String name, String email, String phone, String department, StatusEntity status){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(name != null && !name.isBlank()){
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if(email != null && !email.isBlank()){
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if(phone != null && !phone.isBlank()){
                predicates.add(cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
            }

            if(department != null && !department.isBlank()){
                predicates.add(cb.like(cb.lower(root.get("department")), "%" + department.toLowerCase() + "%"));
            }

            predicates.add(cb.equal(root.get("status"), status));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }

}
