package com.petland.modules.customer.repository;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByEmailAndStatus(String email, StatusEntity status);
}
