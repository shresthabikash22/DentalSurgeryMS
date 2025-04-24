package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
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
}
