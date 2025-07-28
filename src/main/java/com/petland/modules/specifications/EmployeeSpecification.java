package com.petland.modules.specifications;

import com.petland.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeSpecification {

    public static Specification<Employee> Specification(String name, String email, String phone, String department, String status){
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

            if(status!= null && !status.isBlank()){
                predicates.add(cb.like(cb.lower(root.get("status")), "%" + status.toLowerCase() + "%"));
            }

            cb.equal(root.get("status"), StatusEntity.ACTIVE);
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }

}
