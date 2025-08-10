package com.petland.modules.consultation.model;

import com.petland.common.entity.Address;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.consultation.enums.ConsultationPriority;
import com.petland.modules.consultation.enums.ServiceType;
import com.petland.modules.consultation.enums.ConsultationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
@Data
public class ConsultationDetails {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Service status is required")
    private ConsultationStatus consultationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Service Type is required")
    private ServiceType type;

    @Column(nullable = false)
    @NotNull(message = "Service priority is required")
    private ConsultationPriority priority;

    @NotNull(message = "Payment Type is required")
    private PaymentType paymentType;

    @Size(max = 500, message = "Note must be up to 500 characters long.")
    private String notes;

    @Embedded
    @NotNull(message = "Location is required")
    private Address location;

    private LocalDateTime serviceDate = LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal totalByService;

    @Column(nullable = false)
    private BigDecimal profitByService;

    @Column(nullable = false)
    private BigDecimal costOperatingByService;
}
