package com.petland.modules.vaccination.module;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.Pet;
import com.petland.utils.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_vaccinations")
@AllArgsConstructor
@NoArgsConstructor
public class Vaccination extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private Employee veterinarian;

    @OneToMany(mappedBy = "vaccination", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppliedVaccine> appliedVaccines = new ArrayList<>();

    @Embedded
    private Address location;
    private String clinicalNotes;

    @Column(nullable = false)
    private BigDecimal totalCostVaccination;

    @Column(nullable = false)
    private LocalDate vaccinationDate;

    private LocalDate nextDoseDate;
}
