package com.petland.modules.appointment.repository;

import com.petland.modules.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
