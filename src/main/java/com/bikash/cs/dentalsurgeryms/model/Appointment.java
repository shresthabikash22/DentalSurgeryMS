package com.bikash.cs.dentalsurgeryms.model;

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

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date must be today or in the future")
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotBlank(message = "Time is required")
    @Size(max = 8, message = "Time must be up to 8 characters")
    private String time;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status must be up to 20 characters")
    private String status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull(message = "Patient is required")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "dentist_id")
    @NotNull(message = "Dentist is required")
    private Dentist dentist;

    @ManyToOne
    @JoinColumn(name = "surgery_id")
    @NotNull(message = "Surgery is required")
    private Surgery surgery;

}
