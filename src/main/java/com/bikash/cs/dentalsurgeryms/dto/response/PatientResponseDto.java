package com.bikash.cs.dentalsurgeryms.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PatientResponseDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        boolean hasUnpaidBill,
        LocalDate dateOfBirth,
        AddressResponseDto address,
        List<AppointmentResponseDto> appointments
) {
}
