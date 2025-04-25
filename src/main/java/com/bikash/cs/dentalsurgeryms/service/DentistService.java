package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface DentistService {
    DentistResponseDto createDentist(DentistRequestDto dentistRequestDto);
    DentistResponseDto getDentistById(Long id);
    Page<DentistResponseDto> getAllDentists(int page, int pageSize, String sortDirection, String sortBy);
    DentistResponseDto updateDentist(Long id, DentistRequestDto dentistRequestDto);
    void deleteDentist(Long id, Role role);
    public Page<AppointmentResponseDto> getAppointmentsByDentist(Long dentistId, UserDetails userDetails, int page, int pageSize, String sortDirection, String sortBy);

}
