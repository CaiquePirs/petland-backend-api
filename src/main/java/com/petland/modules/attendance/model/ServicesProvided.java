package com.petland.modules.attendance.model;

import com.petland.modules.attendance.enums.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesProvided {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private ServiceType serviceType;

    @Column(nullable = false)
    private BigDecimal servicePrice;

    @Column(nullable = false)
    private int serviceQuantity;

    @Column(nullable = false)
    private BigDecimal serviceTotal;

    @Column(nullable = false)
    private UUID employee_audit;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
