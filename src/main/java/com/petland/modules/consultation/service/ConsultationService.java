package com.petland.modules.consultation.service;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.consultation.calculator.ConsultationCalculator;
import com.petland.modules.consultation.builder.ConsultationFilter;
import com.petland.modules.consultation.dtos.ConsultationHistoryResponseDTO;
import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.dtos.ConsultationResponseDTO;
import com.petland.modules.consultation.enums.ConsultationStatus;
import com.petland.modules.consultation.generate.GenerateConsultationResponse;
import com.petland.modules.consultation.mappers.ConsultationMapper;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.consultation.repositories.ConsultationRepository;
import com.petland.modules.consultation.specifications.ConsultationSpecification;
import com.petland.modules.consultation.strategy.factory.ConsultationFactory;
import com.petland.modules.consultation.validator.ConsultationValidator;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.sale.service.SaleService;
import com.petland.modules.vaccination.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationFactory consultationFactory;
    private final ConsultationValidator validator;
    private final ConsultationMapper mapper;
    private final ConsultationCalculator calculator;
    private final ConsultationRepository repository;
    private final PetService petService;
    private final CustomerService customerService;
    private final EmployeeService employeeService;
    private final AccessValidator accessValidator;
    private final GenerateConsultationResponse generateResponse;
    private final SaleService saleService;
    private final VaccinationService vaccinationService;
    private final PetCareService petCareService;

    @Transactional
    public Consultation registerConsultation(ConsultationRequestDTO requestDTO) {
        Employee employee = employeeService.findById(accessValidator.getLoggedInUser());
        Customer customer = customerService.findById(requestDTO.customerId());
        Pet pet = petService.findById(requestDTO.petId());
        validator.validateIfItIsTheSameCustomer(requestDTO, customer, pet);

        Consultation consultation = mapper.toEntity(requestDTO);
        consultation = consultationFactory.execute(consultation, requestDTO);

        ConsultationDetails details = consultation.getDetails();
        details.setCostOperatingByService(calculator.calculateTotalCostOperating(consultation));
        details.setProfitByService(calculator.calculateTotalProfit(consultation));
        details.setTotalByService(calculator.calculateTotalBilling(consultation));

        consultation.setEmployee(employee);
        consultation.setPet(pet);
        consultation.setCustomer(customer);
        consultation.getCustomer().getConsultationsHistory().add(consultation);
        consultation.getPet().getConsultationsHistory().add(consultation);
        return repository.save(consultation);
    }

    public Consultation findById(UUID consultationId){
        return repository.findById(consultationId)
                .filter(c -> !c.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Consultation ID not found"));

    }

    public Page<ConsultationHistoryResponseDTO> listAllConsultationsByClientId(UUID customerId, Pageable pageable){
        List<ConsultationHistoryResponseDTO> consultationHistory = repository.findAllByCustomerId(customerId, pageable)
                .filter(c -> !c.getStatus().equals(StatusEntity.DELETED))
                .map(generateResponse::mapToCustomerHistory)
                .toList();
        return new PageImpl<>(consultationHistory, pageable, consultationHistory.size());
    }


    public Page<ConsultationResponseDTO> listAllConsultationsByFilter(ConsultationFilter filter, Pageable pageable){
        List<ConsultationResponseDTO> consultationsListByFilter = repository.findAll(
                ConsultationSpecification.filter(filter), pageable)
                .filter(c -> !c.getStatus().equals(StatusEntity.DELETED))
                .map(generateResponse::generateResponse)
                .toList();
        return new PageImpl<>(consultationsListByFilter, pageable, consultationsListByFilter.size());
    }

    public void deactivateConsultationById(UUID consultationId){
        Consultation consultation = findById(consultationId);

        if(consultation.getSales() != null){
            saleService.deactivateSaleById(consultation.getSales().getId());
        }
        if(consultation.getVaccination() != null){
            vaccinationService.deactivateById(consultation.getVaccination().getId());
        }
        if(consultation.getService() != null){
            petCareService.deactivateById(consultation.getService().getId());
        }

        consultation.setStatus(StatusEntity.DELETED);
        repository.save(consultation);
    }

    public void toggleStatusConsultation(UUID consultationId, ConsultationStatus status){
        Consultation consultation = findById(consultationId);
        consultation.getDetails().setConsultationStatus(status);
    }
}
