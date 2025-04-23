package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Appointment saveAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Optional<Appointment> getAppointmentById(Long id);
    void deleteAppointment(Long id);
}
