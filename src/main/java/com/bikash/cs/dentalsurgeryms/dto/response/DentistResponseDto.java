package com.bikash.cs.dentalsurgeryms.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DentistResponseDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String specialization,
        List<AppointmentResponseDto> appointments
) {
}
