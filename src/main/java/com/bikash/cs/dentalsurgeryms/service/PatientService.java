package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    PatientResponseDto createPatient(PatientRequestDto patientRequestDto);
    PatientResponseDto getPatientByEmail(String email);
    Page<PatientResponseDto> getAllPatients(int page, int pageSize, String sortDirection, String sortBy);
    PatientResponseDto updatePatient(String email, PatientRequestDto patientRequestDto);
    void deletePatient(String email);
}
