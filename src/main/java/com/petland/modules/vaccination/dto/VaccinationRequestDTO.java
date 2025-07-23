package com.petland.modules.vaccination.dto;

import com.petland.utils.Address;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record VaccinationRequestDTO(@NotNull(message = "Pet ID is required")
                                    UUID petId,

                                    @NotNull(message = "Owner Id is required")
                                    UUID customerId,

                                    @NotNull(message = "Veterinarian ID is required")
                                    UUID veterinarianId,

                                    @NotNull(message = "Vaccine applied is required")
                                    List<AppliedVaccineRequestDTO> listAppliedVaccineRequestDTO,

                                    @NotNull(message = "Vaccination date is required")
                                    LocalDate vaccinationDate,

                                    LocalDate nextDoseDate,

                                    @NotNull(message = "Vaccination location is required")
                                    Address location,

                                    @DefaultValue("")
                                    String clinicalNotes) {
}
