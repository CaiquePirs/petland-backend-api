package com.petland.modules.vaccination.service;

import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.pet.validator.PetValidator;
import com.petland.modules.vaccination.builder.VaccinationFilter;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationUpdateDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccine;
import com.petland.modules.vaccination.repository.AppliedVaccineRepository;
import com.petland.modules.vaccination.repository.VaccinationRepository;
import com.petland.modules.vaccination.specifications.VaccinationSpecification;
import com.petland.modules.vaccination.util.GenerateVaccinationResponse;
import com.petland.modules.vaccination.calculator.VaccinationCalculator;
import com.petland.modules.vaccination.validator.VaccinationUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final VaccinationCalculator calculator;
    private final VaccinationRepository vaccinationRepository;
    private final VaccinationUpdateValidator validator;
    private final GenerateVaccinationResponse generateResponse;
    private final VaccineService vaccineService;
    private final PetValidator petValidator;

    @Transactional
    public Vaccination register(VaccinationRequestDTO dto){
        Pet pet = petService.findById(dto.petId());
        Customer customer = customerService.findById(dto.customerId());
        Employee veterinarian = employeeService.findById(dto.veterinarianId());
        petValidator.isPetOwner(pet, customer);

        List<AppliedVaccine> appliedVaccines = appliedVaccineService.create(dto.listAppliedVaccineRequestDTO());
        BigDecimal totalCostVaccination = calculator.calculateTotalVaccination(appliedVaccines);
        BigDecimal totalProfit = calculator.calculateProfitByVaccineApplied(appliedVaccines);

        Vaccination vaccination = Vaccination.builder()
                .customer(customer)
                .pet(pet)
                .veterinarian(veterinarian)
                .location(dto.location())
                .clinicalNotes(dto.clinicalNotes())
                .vaccinationDate(dto.vaccinationDate())
                .nextDoseDate(dto.nextDoseDate())
                .appliedVaccines(appliedVaccines)
                .totalByVaccination(totalCostVaccination)
                .profitByVaccination(totalProfit)
                .build();

        appliedVaccines.forEach(v -> v.setVaccination(vaccination));
        pet.getVaccinationsHistory().add(vaccination);
        return vaccinationRepository.save(vaccination);
    }

    public Vaccination findById(UUID vaccinationId) {
        return vaccinationRepository.findById(vaccinationId)
                .filter(v -> !v.getStatus().equals(StatusEntity.DELETED))
                .map(vaccinesApplied -> {
                    List<AppliedVaccine> appliedVaccineList = appliedVaccineRepository.findByVaccinationId(vaccinationId);
                    vaccinesApplied.setAppliedVaccines(appliedVaccineList);
                    return vaccinesApplied;
                }).orElseThrow(() -> new NotFoundException("Vaccination not found"));
    }

    @Transactional
    public void deactivateById(UUID vaccinationId){
        Vaccination vaccination = findById(vaccinationId);

        if(!vaccination.getAppliedVaccines().isEmpty()){
            for(AppliedVaccine appliedVaccine : vaccination.getAppliedVaccines()){
                appliedVaccine.setStatus(StatusEntity.DELETED);
            }
            appliedVaccineRepository.saveAll(vaccination.getAppliedVaccines());
        }
        vaccination.setStatus(StatusEntity.DELETED);
        vaccinationRepository.save(vaccination);
    }

    public Vaccination updateById(VaccinationUpdateDTO dto, UUID vaccinationId){
       Vaccination vaccination = findById(vaccinationId);
       return validator.validate(vaccination, dto);
    }

    public Page<VaccinationResponseDTO> listAllVaccinationsByFilter(VaccinationFilter filter, Pageable pageable) {
        return vaccinationRepository.findAll(VaccinationSpecification.specifications(filter), pageable)
                       .map(generateResponse::generate);
    }

    public List<Vaccination> findAllVaccinationsByPeriod(LocalDate dateMin, LocalDate dateMax) {
        return vaccinationRepository.findAll(VaccinationSpecification.findByPeriod(dateMin, dateMax))
                .stream().filter(v -> !v.getStatus().equals(StatusEntity.DELETED)).toList();
    }

    public List<Vaccination> findAllVaccinationsByVaccine(UUID vaccineId){
        Vaccine vaccine = vaccineService.findById(vaccineId);
        return vaccinationRepository.findAll(VaccinationSpecification.findByVaccine(vaccine))
                .stream().filter(v -> !v.getStatus().equals(StatusEntity.DELETED)).toList();
    }

}