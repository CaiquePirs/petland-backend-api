package com.petland.vaccination;

import com.petland.common.entity.Address;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.vaccination.controller.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.mappers.VaccinationMapperGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class VaccinationMapperGeneratorTest {

    private final VaccinationMapperGenerator generate = new VaccinationMapperGenerator();

    @Test
    void shouldCreateVaccinationResponseSuccessfully(){
        AppliedVaccine applied = AppliedVaccine.builder()
                .id(UUID.randomUUID())
                .vaccine(Vaccine.builder().build())
                .quantityUsed(1)
                .vaccination(Vaccination.builder().id(UUID.randomUUID()).build())
                .build();

        Vaccination vaccination = Vaccination.builder()
                .id(UUID.randomUUID())
                .customer(Customer.builder().id(UUID.randomUUID()).build())
                .veterinarian(Employee.builder().id(UUID.randomUUID()).build())
                .pet(Pet.builder().id(UUID.randomUUID()).build())
                .totalByVaccination(BigDecimal.valueOf(100.00))
                .profitByVaccination(BigDecimal.valueOf(50.00))
                .clinicalNotes("Notes")
                .location(new Address())
                .nextDoseDate(LocalDate.now())
                .vaccinationDate(LocalDate.now())
                .appliedVaccines(List.of(applied))
                .build();

        VaccinationResponseDTO responseDTO = generate.generate(vaccination);

        assertNotNull(responseDTO);
        assertAll(
                () -> assertEquals(vaccination.getId(), responseDTO.getId()),
                () -> assertEquals(vaccination.getPet().getId(), responseDTO.getPetId()),
                () -> assertEquals(vaccination.getCustomer().getId(), responseDTO.getCustomerId()),
                () -> assertEquals(vaccination.getVeterinarian().getId(), responseDTO.getVeterinarianId()),
                () -> assertEquals(vaccination.getLocation(), responseDTO.getLocation()),
                () -> assertEquals(vaccination.getVaccinationDate(), responseDTO.getVaccinationDate()),
                () -> assertEquals(vaccination.getNextDoseDate(), responseDTO.getNextDoseDate()),
                () -> assertEquals(vaccination.getClinicalNotes(), responseDTO.getClinicalNotes()),
                () -> assertEquals(vaccination.getTotalByVaccination(), responseDTO.getTotaByVaccination()),

                // applied vaccine list
                () -> assertEquals(1, responseDTO.getListAppliedVaccineResponseDTO().size()),
                () -> assertEquals(applied.getId(), responseDTO.getListAppliedVaccineResponseDTO().get(0).getId()),
                () -> assertEquals(applied.getVaccination().getId(), responseDTO.getListAppliedVaccineResponseDTO().get(0).getVaccinationId()),
                () -> assertEquals(applied.getVaccine().getId(), responseDTO.getListAppliedVaccineResponseDTO().get(0).getVaccineId()),
                () -> assertEquals(applied.getQuantityUsed(), responseDTO.getListAppliedVaccineResponseDTO().get(0).getQuantityUsed())
        );
    }
}
