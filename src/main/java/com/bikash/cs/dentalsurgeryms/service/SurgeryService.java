package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.model.Surgery;

import java.util.List;
import java.util.Optional;

public interface SurgeryService {
    Surgery saveSurgery(Surgery surgery);
    List<Surgery> getAllSurgeries();
    Optional<Surgery> getSurgeryById(Long id);
    void deleteSurgery(Long id);

}
