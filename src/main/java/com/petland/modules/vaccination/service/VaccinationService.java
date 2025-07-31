package com.petland.modules.vaccination.service;

import com.petland.common.exception.NotFoundException;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.UnauthorizedException;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.vaccination.dto.VaccinationRequestDTO;
import com.petland.modules.vaccination.dto.VaccinationResponseDTO;
import com.petland.modules.vaccination.dto.VaccinationUpdateDTO;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.repository.AppliedVaccineRepository;
import com.petland.modules.vaccination.repository.VaccinationRepository;
import com.petland.modules.vaccination.specifications.VaccinationSpecification;
import com.petland.modules.vaccination.util.GenerateVaccinationResponse;
import com.petland.modules.vaccination.util.VaccinationCalculator;
import com.petland.modules.vaccination.util.VaccinationUpdateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Transactional
    public Vaccination register(VaccinationRequestDTO vaccinationRequestDTO){
        Vaccination vaccination = new Vaccination();
        Pet pet = petService.findPetById(vaccinationRequestDTO.petId());
        Customer customer = customerService.findCustomerById(vaccinationRequestDTO.customerId());
        Employee veterinarian = employeeService.findById(vaccinationRequestDTO.veterinarianId());

        if(!customer.getMyPets().contains(pet)){
            throw new UnauthorizedException("This pet does not belong to this customer");
        }

        vaccination.setPet(pet);
        vaccination.setCustomer(customer);
        vaccination.setVeterinarian(veterinarian);
        vaccination.setLocation(vaccinationRequestDTO.location());
        vaccination.setClinicalNotes(vaccinationRequestDTO.clinicalNotes());
        vaccination.setVaccinationDate(vaccinationRequestDTO.vaccinationDate());
        vaccination.setNextDoseDate(vaccinationRequestDTO.nextDoseDate());

        List<AppliedVaccine> listAppliedVaccine = appliedVaccineService.create(vaccination, vaccinationRequestDTO.listAppliedVaccineRequestDTO());
        BigDecimal totalCostVaccination = calculator.calculateTotalVaccination(listAppliedVaccine);
        BigDecimal totalProfit = calculator.calculateProfitByVaccineApplied(listAppliedVaccine);
        vaccination.setTotalByVaccination(totalCostVaccination);
        vaccination.setProfitByVaccination(totalProfit);
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
    public void deleteById(UUID vaccinationId){
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

    public Vaccination updateVaccination(VaccinationUpdateDTO dto, UUID vaccinationId){
       Vaccination vaccination = findById(vaccinationId);
       return validator.validate(vaccination, dto);
    }

    public Page<VaccinationResponseDTO> listAllVaccinationsByFilter(UUID petId, UUID customerId, UUID veterinarianId, LocalDate vaccinationDate,
                                                         LocalDate nextDoseBefore, StatusEntity status, Pageable pageable){

        List<VaccinationResponseDTO> vaccinationsList = vaccinationRepository.findAll(VaccinationSpecification.specifications(
                petId, customerId, veterinarianId, vaccinationDate, nextDoseBefore, status), pageable).map(generateResponse::generate).toList();

        return new PageImpl<>(vaccinationsList, pageable, vaccinationsList.size());
    }

}