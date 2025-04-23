package com.bikash.cs.dentalsurgeryms.service.impl;


import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.service.DentistService;

import java.util.List;
import java.util.Optional;

public class DentistServiceImpl implements DentistService {

    @Override
    public Dentist saveDentist(Dentist dentist) {
        return null;
    }

    @Override
    public List<Dentist> getAllDentists() {
        return List.of();
    }

    @Override
    public Optional<Dentist> getDentistById(Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteDentist(Long id) {

    }
}
