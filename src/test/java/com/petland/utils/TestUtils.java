package com.petland.utils;

import com.petland.common.entity.Address;
import com.petland.common.entity.enums.Roles;
import com.petland.modules.appointment.model.Appointment;
import com.petland.modules.appointment.model.enums.AppointmentStatus;
import com.petland.modules.consultation.model.enums.ConsultationPriority;
import com.petland.modules.consultation.model.enums.ConsultationStatus;
import com.petland.modules.consultation.model.enums.PaymentType;
import com.petland.modules.consultation.model.enums.ServiceType;
import com.petland.modules.consultation.model.Consultation;
import com.petland.modules.consultation.model.ConsultationDetails;
import com.petland.modules.customer.model.Customer;
import com.petland.modules.employee.enums.Department;
import com.petland.modules.employee.model.Employee;
import com.petland.modules.pet.model.enums.PetGender;
import com.petland.modules.pet.model.enums.PetSpecies;
import com.petland.modules.pet.model.Pet;
import com.petland.modules.petCare.model.enums.PetCareType;
import com.petland.modules.petCare.model.PetCare;
import com.petland.modules.petCare.model.PetCareDetails;
import com.petland.modules.product.model.enums.ProductType;
import com.petland.modules.product.model.Product;
import com.petland.modules.sale.model.ItemsSale;
import com.petland.modules.sale.model.Sale;
import com.petland.modules.vaccination.module.enums.VaccineType;
import com.petland.modules.vaccination.module.AppliedVaccine;
import com.petland.modules.vaccination.module.Vaccination;
import com.petland.modules.vaccination.module.Vaccine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestUtils {
    public static Product product(){
        return Product.builder()
                .stockQuantity(20)
                .costSale(BigDecimal.valueOf(20))
                .costPrice(BigDecimal.valueOf(10))
                .brand("Brand LTDA")
                .productType(ProductType.ACCESSORIES)
                .supplierName("Brand LTDA")
                .description("PetShop products")
                .expirationDate(LocalDate.now().plusMonths(5))
                .manufactureDate(LocalDate.now().minusMonths(5))
                .name("ACCESSORIES")
                .barCode(UUID.randomUUID())
                .build();
    }

    public static Vaccine vaccine(){
        return Vaccine.builder()
                .priceSale(BigDecimal.valueOf(20))
                .purchasePrice(BigDecimal.valueOf(10))
                .vaccineType(VaccineType.RABIES)
                .lotNumber("123455")
                .expirationDate(LocalDate.now().plusMonths(5))
                .manufactureDate(LocalDate.now().minusMonths(5))
                .supplierName("Brand LTDA")
                .stockQuantity(20)
                .build();
    }

    public static Address address(){
        return Address.builder()
                .city("Waterford")
                .zipCode("000 000")
                .state("Waterford")
                .country("Ireland")
                .number("19")
                .street("Street 234")
                .build();
    }

    public static Appointment appointment(){
        return Appointment.builder()
                .appointmentDate(LocalDate.now().minusDays(2))
                .appointmentHour(LocalTime.now())
                .appointmentType(ServiceType.FULL_GROOMING)
                .appointmentStatus(AppointmentStatus.SCHEDULED)
                .build();
    }

    public static Employee employee(){
        return Employee.builder()
                .name("Joao")
                .email("joao@gmail.com")
                .phone("(23) 45 6789-0910")
                .password("joao123456")
                .role(Roles.ADMIN)
                .department(Department.MANAGER)
                .dateBirth(LocalDate.now().minusYears(19))
                .hireDate(LocalDate.now().minusYears(2))
                .address(address())
                .build();
    }

    public static Pet pet(){
        return Pet.builder()
                .specie(PetSpecies.DOG)
                .dateBirth(LocalDate.now().minusYears(1))
                .name("Dog")
                .age(1)
                .breed("Dog")
                .gender(PetGender.MALE)
                .consultationsHistory(new ArrayList<>())
                .servicesHistory(new ArrayList<>())
                .vaccinationsHistory(new ArrayList<>())
                .build();
    }

    public static Customer customer(){
        return Customer.builder()
                .phone("(23) 45 6789-0910")
                .name("Maria")
                .email("maria@gmail.com")
                .address(address())
                .myPets(new ArrayList<>())
                .salesHistory(new ArrayList<>())
                .consultationsHistory(new ArrayList<>())
                .consultationsHistory(new ArrayList<>())
                .role(Roles.CUSTOMER)
                .dateBirth(LocalDate.now().minusYears(18))
                .password("Maria12345")
                .build();
    }

    public static Consultation consultation() {
        return Consultation.builder()
                .details(ConsultationDetails.builder()
                        .location(TestUtils.address())
                        .consultationStatus(ConsultationStatus.OPEN)
                        .paymentType(PaymentType.BANK_TRANSFER)
                        .priority(ConsultationPriority.HIGH)
                        .type(ServiceType.LAB_TEST)
                        .serviceDate(LocalDateTime.now().minusDays(5))
                        .notes("Notes")
                        .totalByService(BigDecimal.valueOf(100))
                        .costOperatingByService(BigDecimal.valueOf(50))
                        .profitByService(BigDecimal.valueOf(50))
                        .build())
                .build();
    }

    public static Sale sale(){
        ItemsSale items = ItemsSale.builder()
                .itemsSaleTotal(BigDecimal.valueOf(50.00))
                .profit(BigDecimal.valueOf(25.00))
                .productQuantity(2)
                .productPrice(BigDecimal.valueOf(25.00))
                .build();

        Sale sale = Sale.builder()
                .itemsSale(List.of(items))
                .totalSales(BigDecimal.valueOf(50.00))
                .profitSale(BigDecimal.valueOf(25.00))
                .paymentType(PaymentType.BANK_TRANSFER)
                .build();
        items.setSale(sale);
        return sale;
    }

    public static PetCare petCare() {
        PetCareDetails details = PetCareDetails.builder()
                .petCareType(PetCareType.CHECKUP)
                .noteService("Notes")
                .unitPrice(BigDecimal.valueOf(10.00))
                .quantityService(2)
                .totalByService(BigDecimal.valueOf(20.00))
                .operatingCost(BigDecimal.valueOf(5.00))
                .profitByService(BigDecimal.valueOf(15.00))
                .build();

        PetCare petCare = PetCare.builder()
                .serviceDate(LocalDateTime.now())
                .location(TestUtils.address())
                .totalRevenue(BigDecimal.valueOf(20.00))
                .totalProfit(BigDecimal.valueOf(15.00))
                .totalCostOperating(BigDecimal.valueOf(5.00))
                .build();
        petCare.setPetCareDetails(List.of(details));
        details.setPetCare(petCare);
        return petCare;
    }

    public static Vaccination vaccination(){
        AppliedVaccine appliedVaccine = AppliedVaccine.builder()
                .quantityUsed(2)
                .build();

        Vaccination vaccination = Vaccination.builder()
                .vaccinationDate(LocalDate.now())
                .nextDoseDate(LocalDate.now().plusMonths(5))
                .totalByVaccination(BigDecimal.valueOf(50.00))
                .profitByVaccination(BigDecimal.valueOf(25.00))
                .location(address())
                .clinicalNotes("Notes")
                .appliedVaccines(List.of(appliedVaccine))
                .build();
        vaccination.getAppliedVaccines().forEach(a -> a.setVaccination(vaccination));
        return vaccination;
    }

}
