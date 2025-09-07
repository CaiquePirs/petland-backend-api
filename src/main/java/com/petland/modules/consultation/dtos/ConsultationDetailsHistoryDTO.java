package com.petland.modules.consultation.dtos;

import com.petland.common.entity.Address;
import com.petland.modules.consultation.enums.ConsultationPriority;
import com.petland.modules.consultation.enums.ConsultationStatus;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.consultation.enums.ServiceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Historical details of a consultation, including type, status, priority, payment, and location.")
public record ConsultationDetailsHistoryDTO(

        @Schema(description = "Status of the consultation.", implementation = ConsultationStatus.class, example = "COMPLETED")
        ConsultationStatus consultationStatus,

        @Schema(description = "Type of service performed during the consultation.", implementation = ServiceType.class, example = "GENERAL_CHECKUP")
        ServiceType type,

        @Schema(description = "Priority level of the consultation.", implementation = ConsultationPriority.class, example = "HIGH")
        ConsultationPriority priority,

        @Schema(description = "Payment method used for the consultation.", implementation = PaymentType.class, example = "CREDIT_CARD")
        PaymentType paymentType,

        @Schema(description = "Additional notes about the consultation.", example = "The pet showed mild symptoms, medication was prescribed.")
        String notes,

        @Schema(description = "Location where the consultation was performed.")
        Address location,

        @Schema(description = "Date and time of the consultation.", example = "2025-09-07T11:00:00")
        LocalDateTime serviceDate
) {}
