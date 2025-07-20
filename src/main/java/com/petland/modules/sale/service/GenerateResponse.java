package com.petland.modules.sale.service;

import com.petland.modules.sale.dtos.ItemsSaleResponseDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenerateResponse {

    public SaleResponseDTO generateSaleResponse(Sale sale){
        List<ItemsSaleResponseDTO> listItemsSaleResponseDTO = generateItemsSaleResponse(sale.getItemsSale());

       SaleResponseDTO saleResponse = SaleResponseDTO.builder()
                .id(sale.getId())
                .customerId(sale.getCustomer().getId())
                .employeeId(sale.getEmployee().getId())
                .paymentType(sale.getPaymentType())
                .totalSales(sale.getTotalSales())
                .itemsSaleResponseDTO(listItemsSaleResponseDTO)
                .build();
       return saleResponse;
    }

    public List<ItemsSaleResponseDTO> generateItemsSaleResponse(List<ItemsSale> listItemsSale){
        List<ItemsSaleResponseDTO> listItemsSaleResponseDTO = new ArrayList<>();

        for(ItemsSale itemsSale : listItemsSale){
           ItemsSaleResponseDTO itemsSaleResponseDTO = ItemsSaleResponseDTO.builder()
                    .id(itemsSale.getId())
                    .saleId(itemsSale.getSale().getId())
                    .productId(itemsSale.getProduct().getId())
                    .productQuantity(itemsSale.getProductQuantity())
                    .itemsSaleTotal(itemsSale.getItemsSaleTotal())
                    .build();
            listItemsSaleResponseDTO.add(itemsSaleResponseDTO);
        }
        return listItemsSaleResponseDTO;
    }




}
