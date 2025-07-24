package com.petland.modules.petCare.model;

import com.petland.common.entity.Address;
import com.petland.common.entity.BaseEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.Pet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_baths")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bath extends BaseEntity {

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
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private int bathQuantity;

    @Column(nullable = false)
    private BigDecimal priceCost;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @Column(nullable = false)
    private LocalDateTime bathMoment;

    private String bathNotes;

    @Embedded
    private Address location;
}
