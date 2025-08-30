package com.petland.petCare;

import com.petland.modules.petCare.calculator.PetCareCalculator;
import com.petland.modules.petCare.dtos.PetCareDetailsRequestDTO;
import com.petland.modules.petCare.enums.PetCareType;
import com.petland.modules.petCare.mappers.PetCareDetailsMapper;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.petCare.service.PetCareDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PetCareDetailsServiceTest {

    private PetCareDetailsMapper mapper;
    private PetCareCalculator calculator;
    private PetCareDetailsService service;

    @BeforeEach
    void setUp(){
        mapper = Mappers.getMapper(PetCareDetailsMapper.class);
        calculator = new PetCareCalculator();
        service = new PetCareDetailsService(calculator, mapper);
    }

    private PetCareDetailsRequestDTO petCareDetails(){
        return PetCareDetailsRequestDTO.builder()
                .petCareType(PetCareType.BATH)
                .noteService("needed a premium special shampoo.")
                .operatingCost(BigDecimal.valueOf(25))
                .quantityService(2)
                .unitPrice(BigDecimal.valueOf(50))
                .build();
    }

    @Test
    void shouldCreatePetCareDetailsSuccessfully(){
        PetCareDetailsRequestDTO dto1 = petCareDetails();
        PetCareDetailsRequestDTO dto2 = petCareDetails();
        PetCareDetailsRequestDTO dto3 = petCareDetails();

        List<PetCareDetailsRequestDTO> listDetailsDTO = new ArrayList<>();
        listDetailsDTO.add(dto1); listDetailsDTO.add(dto2); listDetailsDTO.add(dto3);

        List<PetCareDetails> detailsResult = service.createService(listDetailsDTO);

        BigDecimal totalByServiceExpected = detailsResult.stream()
                .map(PetCareDetails::getTotalByService).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal profitExpected = detailsResult.stream()
                .map(PetCareDetails::getProfitByService).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal costOperatingExpected = detailsResult.stream()
                .map(PetCareDetails::getOperatingCost).reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItemsExpected = detailsResult.stream().mapToInt(PetCareDetails::getQuantityService).sum();

        assertAll(
                () -> assertNotNull(detailsResult),
                () -> assertEquals(3, detailsResult.size()),
                () -> assertEquals(BigDecimal.valueOf(300), totalByServiceExpected),
                () -> assertEquals(BigDecimal.valueOf(225), profitExpected),
                () -> assertEquals(BigDecimal.valueOf(75), costOperatingExpected),
                () -> assertEquals(6, totalItemsExpected)
        );
    }


}
