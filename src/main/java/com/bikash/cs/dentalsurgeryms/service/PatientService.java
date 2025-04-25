package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    PatientResponseDto createPatient(PatientRequestDto patientRequestDto);
    PatientResponseDto getPatientById(Long id);
    Page<PatientResponseDto> getAllPatients(int page, int pageSize, String sortDirection, String sortBy);
    PatientResponseDto updatePatient(Long id, PatientRequestDto patientRequestDto);
    void deletePatient(Long id);
}
