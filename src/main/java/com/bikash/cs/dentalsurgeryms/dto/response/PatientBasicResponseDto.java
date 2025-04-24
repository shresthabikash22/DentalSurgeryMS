package com.bikash.cs.dentalsurgeryms.dto.response;

import java.time.LocalDate;

public record PatientBasicResponseDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email
) {
}
