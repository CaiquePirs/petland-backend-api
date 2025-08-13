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
    private String customerId;
    private String employeeId;
    private String saleId;
    private String vaccinationId;
    private String serviceId;
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

    public UUID getCustomerId() {
        return customerId != null && !customerId.isBlank() ? UUID.fromString(customerId) : null;
    }

    public UUID getSaleId() {
        return saleId != null && !saleId.isBlank() ? UUID.fromString(saleId) : null;
    }

    public UUID getVaccinationId() {
        return vaccinationId != null && !vaccinationId.isBlank() ? UUID.fromString(vaccinationId) : null;
    }

    public UUID getServiceId() {
        return serviceId != null && !serviceId.isBlank() ? UUID.fromString(serviceId) : null;
    }

    public UUID getEmployeeId() {
        return employeeId != null && !employeeId.isBlank() ? UUID.fromString(employeeId) : null;
    }
}
