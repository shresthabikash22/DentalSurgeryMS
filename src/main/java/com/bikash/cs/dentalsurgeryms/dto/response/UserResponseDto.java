package com.bikash.cs.dentalsurgeryms.dto.response;

import java.util.List;

public record UserResponseDto(
        Long userId,
        String username,
        List<String> roles
) {
}
