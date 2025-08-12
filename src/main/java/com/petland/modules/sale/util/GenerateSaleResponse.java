package com.petland.modules.sale.util;

import com.petland.modules.sale.dtos.ItemsSaleResponseDTO;
import com.petland.modules.sale.dtos.SaleResponseDTO;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenerateSaleResponse {

    public SaleResponseDTO generateSaleResponse(Sale sale) {
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

    public List<ItemsSaleResponseDTO> generateItemsSaleResponse(List<ItemsSale> itemsList){
        List<ItemsSaleResponseDTO> listItemsSaleResponseDTO = new ArrayList<>();
        for(ItemsSale item : itemsList){
           ItemsSaleResponseDTO itemsSaleResponseDTO = ItemsSaleResponseDTO.builder()
                    .id(item.getId())
                    .saleId(item.getSale().getId())
                    .productId(item.getProduct().getId())
                    .productQuantity(item.getProductQuantity())
                    .itemsSaleTotal(item.getItemsSaleTotal())
                    .build();
            listItemsSaleResponseDTO.add(itemsSaleResponseDTO);
        }
        return listItemsSaleResponseDTO;
    }
}
