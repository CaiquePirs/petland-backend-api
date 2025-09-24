package com.petland.modules.customer.controller;

import com.petland.common.auth.validator.AccessValidator;
import com.petland.modules.customer.controller.doc.CustomerProfileApi;
import com.petland.modules.appointment.controller.dtos.AppointmentResponseDTO;
import com.petland.modules.appointment.service.AppointmentService;
import com.petland.modules.consultation.controller.dtos.ConsultationHistoryResponseDTO;
import com.petland.modules.consultation.service.ConsultationService;
import com.petland.modules.customer.controller.dto.CustomerResponseDTO;
import com.petland.modules.customer.mappers.CustomerMapper;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.customer.service.CustomerService;
import com.petland.modules.pet.controller.dto.PetResponseDTO;
import com.petland.modules.pet.mapper.PetMapper;
import com.petland.modules.pet.service.PetService;
import com.petland.modules.petCare.controller.dtos.PetCareHistoryResponseDTO;
import com.petland.modules.petCare.service.PetCareService;
import com.petland.modules.sale.controller.dtos.SaleResponseDTO;
import com.petland.modules.sale.service.SaleService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers/me")
@RequiredArgsConstructor
public class CustomerProfileController implements CustomerProfileApi {

    private final AccessValidator accessValidator;
    private final CustomerService customerService;
    private final PetService petService;
    private final PetMapper petMapper;
    private final CustomerMapper customerMapper;
    private final SaleService saleService;
    private final PetCareService petCareService;
    private final ConsultationService consultationService;
    private final AppointmentService appointmentService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> getMyProfile(){
        Customer customer = customerService.findById(accessValidator.getLoggedInUser());
        return ResponseEntity.ok(customerMapper.toDTO(customer));
    }

    @GetMapping("/pets")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PetResponseDTO>> getMyPets(){
        List<PetResponseDTO> myPets = petService.getPetsByCustomerId(accessValidator.getLoggedInUser())
                .stream().map(petMapper::toDTO).toList();
        return ResponseEntity.ok(myPets);
    }

    @GetMapping("/sales/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<SaleResponseDTO>> getMySalesHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        Page<SaleResponseDTO> salesList = saleService.findSalesByCustomerId(
                accessValidator.getLoggedInUser(), PageRequest.of(page, size)
        );

        return ResponseEntity.ok(salesList);
    }

    @GetMapping("/pet-care/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<PetCareHistoryResponseDTO>> getMyServicesHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        Page<PetCareHistoryResponseDTO> servicesHistoryList = petCareService.findAllByCustomerId(
                accessValidator.getLoggedInUser(), PageRequest.of(page, size)
        );
        return ResponseEntity.ok(servicesHistoryList);
    }


    @GetMapping("/consultations/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<ConsultationHistoryResponseDTO>> getMyConsultationsHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        Page<ConsultationHistoryResponseDTO> consultationsHistory = consultationService.listAllConsultationsByClientId(
                accessValidator.getLoggedInUser(), PageRequest.of(page, size)
        );
        return ResponseEntity.ok(consultationsHistory);
    }

    @GetMapping("/appointments/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<AppointmentResponseDTO>> getMyAppointmentsHistory(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size){

        Page<AppointmentResponseDTO> appointmentsHistory = appointmentService.listAllAppointmentsByCustomerId(
                accessValidator.getLoggedInUser(), PageRequest.of(page, size)
        );
        return ResponseEntity.ok(appointmentsHistory);
    }
}
