package com.bikash.cs.dentalsurgeryms.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record DentistRequestDto(
        @NotBlank(message = "First name cannot be blank")
        @Size(max = 50, message = "First name must be ≤ 50 characters")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(max = 50, message = "Last name must be ≤ 50 characters")
        String lastName,

        @NotBlank(message = "Phone number cannot be blank")
        @Size(max = 20, message = "Phone number must be ≤ 20 characters")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone format")
        String phoneNumber,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Specialization cannot be blank")
        @Size(max = 100, message = "Specialization must be ≤ 100 characters")
        String specialization,

        Long userId
) {
}
