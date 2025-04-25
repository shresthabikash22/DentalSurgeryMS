package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import org.springframework.data.domain.Page;

public interface AppointmentService {
    com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentResponseDto);
    com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto getAppointmentById(Long id);
    Page<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> getAppointmentsByPatientId(Long patientId, int page, int pageSize, String sortDirection, String sortBy);
    Page<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> getAppointmentsByDentistId(Long dentistId, int page, int pageSize, String sortDirection, String sortBy);
    com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto cancelAppointment(Long id);
    com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto updateAppointment(Long id, AppointmentRequestDto appointmentResponseDto);
    void deleteAppointment(Long id);
    public boolean hasAppointmentsForSurgery(Long surgeryId);
    public boolean hasAppointmentsForDentistAndStatusNot(Dentist dentist, AppointmentStatus status );
    public boolean hasAppointmentsForPatientAndStatusNot(Patient patient, AppointmentStatus status);
}
