package com.petland.modules.appointment.strategy;

import com.petland.modules.appointment.model.Appointment;

public interface AppointmentStrategy<T> {
    T generate(Appointment appointment);
}
