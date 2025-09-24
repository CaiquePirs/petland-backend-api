package com.petland.modules.consultation.strategy.impl;

import com.petland.modules.consultation.controller.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.strategy.ConsultationStrategy;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SalesConsultationImpl implements ConsultationStrategy {

    private final SaleService saleService;

    public Consultation execute(Consultation consultation, ConsultationRequestDTO requestDTO) {
        if(requestDTO.saleRequestDTO() != null){
            Sale sale = saleService.registerSale(requestDTO.saleRequestDTO());
            consultation.setSales(sale);
        }
        return consultation;
    }
}
