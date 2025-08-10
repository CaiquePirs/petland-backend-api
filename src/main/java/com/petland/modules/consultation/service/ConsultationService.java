package com.petland.modules.consultation.service;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.common.entity.enums.StatusEntity;
import com.petland.common.exception.NotFoundException;
import com.petland.modules.consultation.calculator.ConsultationCalculator;
import com.petland.modules.consultation.dtos.ConsultationRequestDTO;
import com.petland.modules.consultation.mappers.ConsultationMapper;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.repositories.ConsultationRepository;
import com.petland.modules.consultation.strategy.factory.ConsultationFactory;
import com.petland.modules.consultation.validator.ConsultationValidator;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.employee.service.EmployeeService;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Consultation registerConsultation(ConsultationRequestDTO requestDTO) {
        Employee employee = employeeService.findById(accessValidator.getLoggedInUser());
        Customer customer = customerService.findById(requestDTO.customerId());
        Pet pet = petService.findById(requestDTO.petId());
        validator.validateIfItIsTheSameCustomer(requestDTO, customer, pet);

        Consultation consultation = mapper.toEntity(requestDTO);
        consultation.setEmployee(employee);
        consultation.setCustomer(customer);
        consultation.setPet(pet);

        consultation = consultationFactory.execute(consultation, requestDTO);

        if(consultation.getDetails() != null) {
            consultation.getDetails().setTotalByService(calculator.calculateTotalBilling(consultation));
            consultation.getDetails().setCostOperatingByService(calculator.calculateTotalCostOperating(consultation));
            consultation.getDetails().setProfitByService(calculator.calculateTotalProfit(consultation));
        }

        consultation.getCustomer().getConsultationsHistory().add(consultation);
        consultation.getPet().getConsultationsHistory().add(consultation);
        return repository.save(consultation);
    }

    public Consultation findById(UUID consultationId){
        return repository.findById(consultationId)
                .filter(c -> !c.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Consultation ID not found"));

    }
}
