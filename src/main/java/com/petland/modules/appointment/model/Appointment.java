package com.petland.modules.appointment.model;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.consultation.enums.ServiceType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.pet.model.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "tb_appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Pet pet;

    @Column(nullable = false)
    private LocalTime appointmentHour;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType appointmentType;
}
