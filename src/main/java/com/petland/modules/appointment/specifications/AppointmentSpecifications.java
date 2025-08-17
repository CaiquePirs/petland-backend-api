package com.petland.modules.appointment.specifications;

import com.petland.modules.appointment.builder.AppointmentFilter;
import com.petland.modules.appointment.model.Appointment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecifications {

    public static Specification<Appointment> specification(AppointmentFilter filter) {
        if (filter == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), filter.getCustomerId()));
            }
            if (filter.getPetId() != null) {
                predicates.add(cb.equal(root.get("pet").get("id"), filter.getPetId()));
            }
            if (filter.getAppointmentStatus() != null) {
                predicates.add(cb.equal(root.get("appointmentStatus"), filter.getAppointmentStatus()));
            }
            if (filter.getAppointmentHour() != null) {
                predicates.add(cb.equal(root.get("appointmentHour"), filter.getAppointmentHour()));
            }
            if (filter.getAppointmentDate() != null) {
                predicates.add(cb.equal(root.get("appointmentDate"), filter.getAppointmentDate()));
            }
            if (filter.getAppointmentType() != null) {
                predicates.add(cb.equal(root.get("appointmentType"), filter.getAppointmentType()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Appointment> appointmentAtSameTimeSpec(LocalDate date, LocalTime hour) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("appointmentDate"), date),
                cb.equal(root.get("appointmentHour"), hour)
        );
    }
}
