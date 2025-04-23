package com.bikash.cs.dentalsurgeryms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "surgeries")
@Data
public class Surgery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String surgeryName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must be up to 20 characters")
    private String phoneNumber;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    @NotNull(message = "Address is required")
    private Address address;


    @OneToMany(mappedBy = "surgery", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();
}
