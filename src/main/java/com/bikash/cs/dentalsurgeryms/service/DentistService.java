package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.model.Dentist;

import java.util.List;
import java.util.Optional;

public interface DentistService {
    Dentist saveDentist(Dentist dentist);
    List<Dentist> getAllDentists();
    Optional<Dentist> getDentistById(Long id);
    void deleteDentist(Long id);
}
