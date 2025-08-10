package com.petland.modules.consultation.repositories;

import com.petland.modules.consultation.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
}
