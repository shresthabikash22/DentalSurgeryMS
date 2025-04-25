package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DentistService {
    DentistResponseDto createDentist(DentistRequestDto dentistRequestDto);
    DentistResponseDto getDentistById(Long id);
    Page<DentistResponseDto> getAllDentists(int page, int pageSize, String sortDirection, String sortBy);
    DentistResponseDto updateDentist(Long id, DentistRequestDto dentistRequestDto);
    void deleteDentist(Long id);
}
