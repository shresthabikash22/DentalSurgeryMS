package com.bikash.cs.dentalsurgeryms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "addresses")
@Builder
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Street is required")
    @Size(max = 100, message = "Street must be up to 100 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must be up to 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must be up to 50 characters")
    private String state;

    @NotBlank(message = "Zip is required")
    @Size(max = 10, message = "Zip must be up to 10 characters")
    private String zipCode;

    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zip;
    }


}
