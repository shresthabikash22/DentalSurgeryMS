package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.model.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistRepository extends JpaRepository<Dentist,Long> {
}
