package com.petland.modules.sale.model;

import com.petland.common.entity.BaseEntity;
import com.petland.modules.attendance.enums.PaymentType;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_sales")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<ItemsSale> itemsSale;

    private BigDecimal totalSales;

    private BigDecimal profitSale;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

}

