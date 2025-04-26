package com.bikash.cs.dentalsurgeryms.dto.response;

public record AddressResponseDto(
        Long addressId,
        String street,
        String city,
        String state,
        String zipCode
) {
}
