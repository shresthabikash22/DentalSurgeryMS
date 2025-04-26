package com.bikash.cs.dentalsurgeryms.dto.request;

import com.bikash.cs.dentalsurgeryms.enums.Role;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record RegisterRequestDto(

        // common attributes
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


        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @Valid
        AddressRequestDto address,

         // dentist  specific attribute
        @Size(max = 100, message = "Specialization must be up to 100 characters")
        @Column(nullable = false)
         String specialization,

        // user specific attributes

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String password,

        Role role
) {
}
