package com.bikash.cs.dentalsurgeryms.service;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    AddressResponseDto createAddress(AddressRequestDto addressRequestDto);
    AddressResponseDto getAddressById(Long id);
    Page<AddressResponseDto> getAllAddresses(int page,int pageSize,String sortDirection,String sortBy);
    AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto);
    void deleteAddress(Long id);
}
