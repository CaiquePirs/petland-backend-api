package com.petland.modules.petCare.repositories;

import com.petland.modules.petCare.model.Grooming;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroomingRepository extends JpaRepository<Grooming, UUID> {
}
