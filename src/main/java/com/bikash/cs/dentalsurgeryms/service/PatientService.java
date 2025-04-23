package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient savePatient(Patient patient);
    List<Patient> getAllPatients();
    Optional<Patient> getPatientById(Long id);
    void deletePatient(Long id);
}
