package com.petland.modules.consultation.model;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.sale.model.Sale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_consultations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "sale_id", nullable = true)
    private Sale sales;

    @OneToOne
    @JoinColumn(name = "vaccination_id", nullable = true)
    private Vaccination vaccination;

    @OneToOne
    @JoinColumn(name = "petcare_service_id", nullable = true)
    private PetCare service;

    @Embedded
    private ConsultationDetails details;
}
