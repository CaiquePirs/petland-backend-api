package com.petland.modules.vaccination.validator;

import com.petland.modules.vaccination.dto.VaccineUpdateDTO;
import com.petland.modules.vaccination.module.Vaccine;
import org.springframework.stereotype.Component;

@Component
public class VaccineUpdateValidator {

    public Vaccine validate(Vaccine vaccine, VaccineUpdateDTO dto) {
        if (dto.lotNumber() != null) vaccine.setLotNumber(dto.lotNumber());
        if (dto.supplierName() != null) vaccine.setSupplierName(dto.supplierName());
        if (dto.vaccineType() != null) vaccine.setVaccineType(dto.vaccineType());
        if (dto.purchasePrice() != null) vaccine.setPurchasePrice(dto.purchasePrice());
        if (dto.priceSale() != null) vaccine.setPriceSale(dto.priceSale());
        if (dto.stockQuantity() != null) vaccine.setStockQuantity(dto.stockQuantity());
        if (dto.manufactureDate() != null) vaccine.setManufactureDate(dto.manufactureDate());
        if (dto.expirationDate() != null) vaccine.setExpirationDate(dto.expirationDate());
        return vaccine;
    }
}
