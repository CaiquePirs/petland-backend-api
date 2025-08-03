package com.petland.modules.petCare.repositories;

import com.petland.modules.petCare.model.PetCare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PetCareRepository extends JpaRepository<PetCare, UUID>, JpaSpecificationExecutor<PetCare> {
    Page<PetCare> findByCustomerId(UUID customerId, Pageable pageable);
}
