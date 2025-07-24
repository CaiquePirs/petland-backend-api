package com.petland.modules.petCare.repositories;

import com.petland.modules.petCare.model.Bath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BathRepository extends JpaRepository<Bath, UUID> {
}
