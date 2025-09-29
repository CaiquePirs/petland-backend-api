package com.petland.modules.vaccination.repository;

import com.petland.modules.vaccination.module.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VaccinationRepository extends JpaRepository<Vaccination, UUID>, JpaSpecificationExecutor<Vaccination> {

    @Query("SELECT v FROM Vaccination v JOIN AppliedVaccine av ON v.id = av.vaccination.id WHERE av.vaccination.id = :vaccinationId")
    Optional<Vaccination> findByVaccinationId(@Param("vaccinationId") UUID vaccinationId);
}
