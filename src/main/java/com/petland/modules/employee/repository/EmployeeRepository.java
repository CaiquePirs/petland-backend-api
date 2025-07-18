package com.petland.modules.employee.repository;

import com.petland.enums.StatusEntity;
import com.petland.modules.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmailAndStatus(String email, StatusEntity status);
}
