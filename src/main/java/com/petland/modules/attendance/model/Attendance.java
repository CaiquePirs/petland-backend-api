package com.petland.modules.attendance.model;

import com.petland.utils.Address;
import com.petland.enums.StatusEntity;
import com.petland.modules.attendance.enums.PaymentType;
import com.petland.modules.attendance.enums.StatusService;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.sale.model.Sale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_attendances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

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

    @OneToMany
    @JoinColumn(name = "sale_id")
    private List<Sale> sales;

    @Column(nullable = false)
    private String notes;

    @Embedded
    private Address location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private BigDecimal paymentTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusService statusService;

    @Enumerated(EnumType.STRING)
    private StatusEntity status;

    private UUID employeeAudit;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
