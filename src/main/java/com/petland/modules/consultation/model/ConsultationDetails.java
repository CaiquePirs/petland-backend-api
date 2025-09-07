package com.petland.modules.consultation.model;

import com.petland.common.entity.Address;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.consultation.enums.ConsultationPriority;
import com.petland.modules.consultation.enums.ServiceType;
import com.petland.modules.consultation.enums.ConsultationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Additional details about the consultation, including type, status, priority, and financials.")
public class ConsultationDetails {

    @NotNull(message = "Service status is required")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status of the consultation.", implementation = ConsultationStatus.class, example = "COMPLETED")
    private ConsultationStatus consultationStatus;

    @NotNull(message = "Service type is required")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Type of service performed during the consultation.", implementation = ServiceType.class, example = "GENERAL_CHECKUP")
    private ServiceType type;

    @NotNull(message = "Service priority is required")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Priority level of the consultation.", implementation = ConsultationPriority.class, example = "HIGH")
    private ConsultationPriority priority;

    @NotNull(message = "Payment type is required")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Payment method used for the consultation.", implementation = PaymentType.class, example = "CASH")
    private PaymentType paymentType;

    @Size(max = 500, message = "Note must be up to 500 characters long.")
    @Schema(description = "Additional notes about the consultation.", example = "The pet required special attention due to a pre-existing condition.")
    private String notes;

    @NotNull(message = "Location is required")
    @Embedded
    @Schema(description = "Location where the consultation was performed.")
    private Address location;

    @Schema(description = "Date and time of the consultation.", example = "2025-09-07T10:30:00")
    private LocalDateTime serviceDate = LocalDateTime.now();

    @Schema(description = "Total cost of the consultation service.", example = "300.00")
    private BigDecimal totalByService;

    @Schema(description = "Profit from the consultation service.", example = "120.00")
    private BigDecimal profitByService;

    @Schema(description = "Operating cost of the consultation service.", example = "180.00")
    private BigDecimal costOperatingByService;
}