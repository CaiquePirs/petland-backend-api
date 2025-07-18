package com.petland.modules.customer.repository;

import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmailAndStatus(String email, StatusEntity status);
}
