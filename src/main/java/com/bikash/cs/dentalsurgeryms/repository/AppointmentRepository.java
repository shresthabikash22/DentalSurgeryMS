package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    Page<Appointment> findByDentistId(Long dentistId, Pageable pageable);

    List<Appointment> findByDentistIdAndAppointmentDateTimeBetweenAndStatus(Long dentistId, LocalDateTime start, LocalDateTime end, AppointmentStatus status);

    boolean existsBySurgery_Id(Long surgeryId);
    boolean existsByDentistAndStatusNot(Dentist dentist, AppointmentStatus status);
    boolean existsByPatientAndStatusNot(Patient patient, AppointmentStatus status);
}
