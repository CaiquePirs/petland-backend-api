package com.petland.modules.vaccination.repository;

import com.petland.modules.vaccination.module.AppliedVaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppliedVaccineRepository extends JpaRepository<AppliedVaccine, UUID> {
    List<AppliedVaccine> findByVaccinationId(UUID vaccinationId);
}
