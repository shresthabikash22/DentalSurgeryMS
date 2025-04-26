package com.bikash.cs.dentalsurgeryms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(
        name = "surgeries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"surgeryName", "address_id"})
)
@Data
@Builder
@AllArgsConstructor
public class Surgery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be up to 100 characters")
    private String surgeryName;

    @NotBlank(message = "Branch code is required")
    @Pattern(regexp = "^[A-Z]{2}-[A-Z0-9]{2,10}$", message = "Branch code must be in format 'XX-YYYY' (e.g., SC-DT)")
    @Column(nullable = false, unique = true)
    private String branchCode;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must be up to 20 characters")
    private String phoneNumber;

    @OneToOne(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @NotNull(message = "Address is required")
    private Address address;

    @Builder.Default
    @OneToMany(mappedBy = "surgery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public Surgery(String surgeryName, String branchCode, String phoneNumber, Address address) {
        this.surgeryName = surgeryName;
        this.branchCode = branchCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public void addAppointment(Appointment appointment) {
        if (appointment != null) {
            appointments.add(appointment);
            appointment.setSurgery(this);
        }
    }

    public void removeAppointment(Appointment appointment) {
        if (appointment != null) {
            appointments.remove(appointment);
            appointment.setSurgery(null);
        }
    }
}
