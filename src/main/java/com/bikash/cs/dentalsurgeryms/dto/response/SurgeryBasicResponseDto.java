package com.bikash.cs.dentalsurgeryms.dto.response;

public record SurgeryBasicResponseDto(
        Long id,
        String name,
        String branchCode,
        String location
) {
}
