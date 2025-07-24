package com.petland.modules.vaccination.service;

import com.petland.common.exception.NotFoundException;
import com.petland.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.repository.AppliedVaccineRepository;
import com.petland.modules.vaccination.repository.VaccinationRepository;
import com.petland.modules.vaccination.util.CalculateTotalVaccinationCost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccinationService {

    private final EmployeeService employeeService;
    private final CustomerService customerService;
    private final PetService petService;
    private final AppliedVaccineService appliedVaccineService;
    private final AppliedVaccineRepository appliedVaccineRepository;
    private final CalculateTotalVaccinationCost calculateTotalVaccinationCost;
    private final VaccinationRepository vaccinationRepository;

    @Transactional
    public Vaccination register(VaccinationRequestDTO vaccinationRequestDTO){
        Vaccination vaccination = new Vaccination();
        Pet pet = petService.findPetById(vaccinationRequestDTO.petId());
        Customer customer = customerService.findCustomerById(vaccinationRequestDTO.customerId());
        Employee veterinarian = employeeService.findById(vaccinationRequestDTO.veterinarianId());

        vaccination.setPet(pet);
        vaccination.setCustomer(customer);
        vaccination.setVeterinarian(veterinarian);
        vaccination.setLocation(vaccinationRequestDTO.location());
        vaccination.setStatus(StatusEntity.ACTIVE);
        vaccination.setClinicalNotes(vaccinationRequestDTO.clinicalNotes());
        vaccination.setVaccinationDate(vaccinationRequestDTO.vaccinationDate());
        vaccination.setNextDoseDate(vaccinationRequestDTO.nextDoseDate());

        List<AppliedVaccine> listAppliedVaccine = appliedVaccineService.create(vaccination, vaccinationRequestDTO.listAppliedVaccineRequestDTO());
        BigDecimal totalCostVaccination = calculateTotalVaccinationCost.calculate(listAppliedVaccine);
        vaccination.setTotalCostVaccination(totalCostVaccination);

        return vaccinationRepository.save(vaccination);
    }

    public Vaccination findById(UUID vaccinationId){
        Vaccination vaccination = vaccinationRepository.findById(vaccinationId)
                .orElseThrow(() -> new NotFoundException("Vaccination not found"));

        if (vaccination.getStatus().equals(StatusEntity.DELETED)) {
            throw new NotFoundException("Vaccination ID not found");
        }

        List<AppliedVaccine> appliedVaccineList = appliedVaccineRepository.findByVaccinationId(vaccinationId);
        vaccination.setAppliedVaccines(appliedVaccineList);
        return vaccination;
    }

}