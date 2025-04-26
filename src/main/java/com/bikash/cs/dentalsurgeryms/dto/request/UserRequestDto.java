package com.bikash.cs.dentalsurgeryms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserRequestDto (
        @NotBlank(message = "Username cannot be blank/empty/null")
        @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
        String username,

        @NotBlank(message = "Password cannot be blank/empty/null")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String password,

        List<String> roles
){
}
