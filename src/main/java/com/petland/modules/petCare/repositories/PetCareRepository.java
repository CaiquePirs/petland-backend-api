package com.petland.modules.petCare.repositories;

import com.petland.modules.petCare.model.PetCare;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PetCareRepository extends JpaRepository<PetCare, UUID> {
}
