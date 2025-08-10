package com.petland.modules.consultation.dtos;

import com.petland.common.entity.Address;
import com.petland.modules.consultation.enums.ConsultationPriority;
import com.petland.modules.consultation.enums.ConsultationStatus;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.consultation.enums.ServiceType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ConsultationDetailsHistoryDTO(ConsultationStatus consultationStatus,
                                            ServiceType type,
                                            ConsultationPriority priority,
                                            PaymentType paymentType,
                                            String notes,
                                            Address location,
                                            LocalDateTime serviceDate) {
}
