package com.bikash.cs.dentalsurgeryms.service;


import com.bikash.cs.dentalsurgeryms.dto.request.SurgeryRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import org.springframework.data.domain.Page;

public interface SurgeryService {

    SurgeryResponseDto createSurgery(SurgeryRequestDto surgeryRequestDto);
    SurgeryResponseDto getSurgeryByBranchCode(String branchCode);
    Page<SurgeryResponseDto> getAllSurgeries(int page,int pageSize,String sortDirection,String sortBy);
    SurgeryResponseDto updateSurgery(String branchCode, SurgeryRequestDto surgeryRequestDto);
    void deleteSurgery(String branchCode);

}
