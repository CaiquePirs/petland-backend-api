package com.petland.vaccination;

import com.petland.common.exception.InsufficientStockException;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.vaccination.dto.AppliedVaccineRequestDTO;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.service.AppliedVaccineService;
import com.petland.modules.vaccination.service.VaccineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppliedVaccineServiceTest {

    @Mock
    private VaccineService vaccineService;

    @InjectMocks
    AppliedVaccineService service;

    private AppliedVaccineRequestDTO dto;
    private Vaccine vaccine;

    @BeforeEach
    void setUp(){
        dto = new AppliedVaccineRequestDTO(UUID.randomUUID(), 5);
        vaccine = new Vaccine();
    }

    @Test
    void shouldCreateAppliedVaccineSuccessfully(){
        vaccine.setStockQuantity(10);
        vaccine.setId(dto.vaccineId());

        when(vaccineService.updateStock(dto.quantityUsed(), dto.vaccineId())).thenReturn(vaccine);

        List<AppliedVaccine> result = service.create(List.of(dto));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(dto.vaccineId(), result.get(0).getVaccine().getId())
        );
        verify(vaccineService).updateStock(dto.quantityUsed(), dto.vaccineId());
    }

    @Test
    void shouldThrowExceptionWhenVaccineIsNotFound(){
        doThrow(new NotFoundException("Vaccine ID not found"))
                .when(vaccineService).updateStock(dto.quantityUsed(), dto.vaccineId());

       NotFoundException ex = assertThrows(
               NotFoundException.class,
               ()-> service.create(List.of(dto))
       );
       assertEquals("Vaccine ID not found", ex.getMessage());

       verify(vaccineService).updateStock(dto.quantityUsed(), dto.vaccineId());
    }

    @Test
    void shouldThrowExceptionWhenVaccineStockIsInsufficient(){
        doThrow(new InsufficientStockException("Vaccine stock is insufficient"))
                .when(vaccineService).updateStock(dto.quantityUsed(), dto.vaccineId());

        InsufficientStockException ex = assertThrows(
                InsufficientStockException.class,
                ()-> service.create(List.of(dto))
        );
        assertEquals("Vaccine stock is insufficient", ex.getMessage());

        verify(vaccineService).updateStock(dto.quantityUsed(), dto.vaccineId());
    }

}
