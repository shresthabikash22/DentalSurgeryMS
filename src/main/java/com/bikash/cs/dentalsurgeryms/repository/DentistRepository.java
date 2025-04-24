package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.model.Dentist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DentistRepository extends JpaRepository<Dentist,Long> {
    Optional<Dentist> findByEmail(String email);
    Page<Dentist> findAll(Pageable pageable);

    Optional<Dentist> findByUser_UserId(Long userId);
}
