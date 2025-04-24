package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DentistService {
    DentistResponseDto createDentist(DentistRequestDto dentistRequestDto);
    DentistResponseDto getDentistByEmail(String email);
    Page<DentistResponseDto> getAllDentists(int page, int pageSize, String sortDirection, String sortBy);
    DentistResponseDto updateDentist(String email, DentistRequestDto dentistRequestDto);
    void deleteDentist(String email);
}
