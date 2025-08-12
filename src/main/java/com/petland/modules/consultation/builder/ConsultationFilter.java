package com.petland.modules.consultation.builder;

import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.consultation.enums.ConsultationPriority;
import com.petland.modules.consultation.enums.ConsultationStatus;
import com.petland.modules.consultation.enums.PaymentType;
import com.petland.modules.consultation.enums.ServiceType;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ConsultationFilter {
    private UUID customerId;
    private UUID employeeId;
    private UUID saleId;
    private UUID vaccinationId;
    private UUID serviceId;
    private ConsultationStatus consultationStatus;
    private ServiceType type;
    private ConsultationPriority priority;
    private PaymentType paymentType;
    private BigDecimal totalByService;
    private BigDecimal profitByService;
    private BigDecimal costOperatingByService;
    private StatusEntity status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime serviceDateFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime serviceDateTo;

    public String getStatus(){
        return status.toString().toUpperCase();
    }
}
