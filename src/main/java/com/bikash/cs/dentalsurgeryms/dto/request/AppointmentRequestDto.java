package com.bikash.cs.dentalsurgeryms.dto.request;

import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequestDto(
        @Future(message = "Appointment date and time must be in the future")
        @NotNull(message = "Appointment date and time cannot be null")
        LocalDateTime appointmentDateTime,

        @NotNull(message = "Status cannot be null")
        AppointmentStatus status,

        @NotNull(message = "Patient ID cannot be null")
        Long patientId,

        @NotNull(message = "Dentist ID cannot be null")
        Long dentistId,

        @NotNull(message = "Surgery ID cannot be null")
        Long surgeryId
) {

}
