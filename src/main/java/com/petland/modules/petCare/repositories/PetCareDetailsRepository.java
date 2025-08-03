package com.petland.modules.petCare.repositories;

import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.model.PetCareDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PetCareDetailsRepository extends JpaRepository<PetCareDetails, UUID> {
    List<PetCareDetails> findByPetCareType(PetCareType petCareType);
}
