package com.petland.modules.vaccination.repository;

import com.petland.modules.vaccination.module.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VaccinationRepository extends JpaRepository<Vaccination, UUID> {
}
