package com.petland.modules.vaccination.repository;

import com.petland.modules.vaccination.module.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VaccineRepository extends JpaRepository<Vaccine, UUID> {
}
