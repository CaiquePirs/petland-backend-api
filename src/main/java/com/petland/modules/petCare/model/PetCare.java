package com.petland.modules.petCare.model;

import com.petland.common.entity.Address;
import com.petland.common.entity.BaseEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_petcare_services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetCare extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "petCare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetCareDetails> petCareDetails = new ArrayList<>();

    @Column(nullable = false)
    private BigDecimal totalRevenue;

    @Column(nullable = false)
    private BigDecimal totalCostOperating;

    @Column(nullable = false)
    private BigDecimal totalProfit;

    private LocalDateTime serviceDate;

    @Embedded
    private Address location;
}
