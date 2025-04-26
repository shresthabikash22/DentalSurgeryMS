package com.bikash.cs.dentalsurgeryms.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SurgeryRequestDto(
        @NotBlank(message = "Branch code cannot be blank/empty/null")
        @Pattern(regexp = "^[A-Z]{2}-[A-Z0-9]{2,10}$", message = "Branch code must be in format 'XX-YYYY' (e.g., SC-DT)")
        String branchCode,

        @NotBlank(message = "Name cannot be blank/empty/null")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Phone number cannot be blank/empty/null")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be a valid international format")
        String phoneNumber,

        @Valid
        @NotNull(message = "Address cannot be null")
        AddressRequestDto addressRequestDto
) {
}
