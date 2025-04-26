package com.bikash.cs.dentalsurgeryms.dto.response;

public record SurgeryResponseDto(
        Long surgeryId,
        String branchCode,
        String name,
        String phoneNumber,
        AddressResponseDto addressResponseDto
) {
}
