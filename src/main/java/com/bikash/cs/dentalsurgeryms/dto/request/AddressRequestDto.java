package com.bikash.cs.dentalsurgeryms.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDto(
        @NotBlank(message = "Street is mandatory") String street,
        @NotBlank(message = "City is mandatory") String city,
        @NotBlank(message = "State is mandatory") String state,
        @NotBlank(message = "Zip code is mandatory") String zipCode
) {
}
