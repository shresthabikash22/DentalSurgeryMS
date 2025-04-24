package com.bikash.cs.dentalsurgeryms.model;

import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name = "appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Appointment date and time cannot be null")
    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference(value = "patient-appointment")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "dentist_id", nullable = false)
    @JsonBackReference(value = "dentist-appointment")
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "surgery_id", nullable = false)
    @JsonBackReference(value = "surgery-appointment")
    private Surgery surgery;

    public Appointment(LocalDateTime appointmentDateTime, AppointmentStatus status, Patient patient, Dentist dentist, Surgery surgery) {
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.patient = patient;
        this.dentist = dentist;
        this.surgery = surgery;
    }

}
