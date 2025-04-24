package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto);
    AppointmentResponseDto getAppointmentById(Long id);
    Page<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId, int page, int pageSize, String sortDirection, String sortBy);
    Page<AppointmentResponseDto> getAppointmentsByDentistId(Long dentistId, int page, int pageSize, String sortDirection, String sortBy);
    AppointmentResponseDto cancelAppointment(Long id);
    AppointmentResponseDto updateAppointment(Long id, AppointmentRequestDto appointmentRequestDto);
    void deleteAppointment(Long id);
    public boolean hasAppointmentsForSurgery(Long surgeryId);
    public boolean hasAppointmentsForDentistAndStatusNot(Dentist dentist, AppointmentStatus status );
    public boolean hasAppointmentsForPatientAndStatusNot(Patient patient, AppointmentStatus status);
}
