package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findById(Long id);
    Page<Patient> findAll(Pageable pageable);
    Optional<Patient> findByUser_UserId(Long userId);
}
