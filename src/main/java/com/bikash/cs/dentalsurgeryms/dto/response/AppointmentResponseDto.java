package com.bikash.cs.dentalsurgeryms.dto.response;

import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AppointmentResponseDto(
        Long id,
        LocalDateTime appointmentDateTime,
        AppointmentStatus status,
        PatientResponseDto patient,
        DentistResponseDto dentist,
        SurgeryResponseDto surgery
) {
}
