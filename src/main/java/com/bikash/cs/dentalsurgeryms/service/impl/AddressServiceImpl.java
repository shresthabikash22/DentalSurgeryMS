package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.exception.general.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.AddressMapper;
import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.repository.AddressRepository;
import com.bikash.cs.dentalsurgeryms.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
        Address address = addressMapper.addressRequestDtoToAddress(addressRequestDto);
        return addressMapper.addressToAddressResponseDto(addressRepository.save(address));
    }

    @Override
    public AddressResponseDto getAddressById(Long id) {
        Address address = addressRepository.findById(id).orElse(null);
        return addressMapper.addressToAddressResponseDto(address);
    }

    @Override
    public Page<AddressResponseDto> getAllAddresses(int page,int pageSize,String sortDirection,String sortBy) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortBy
        );
        Page<Address> addressesPage = addressRepository.findAll(pageable);
        return addressesPage.map(addressMapper::addressToAddressResponseDto);
    }

    @Override
    public AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id '" + id + "' not found"));

        Address updatedAddress = addressMapper.addressRequestDtoToAddress(addressRequestDto);
        updatedAddress.setId(existingAddress.getId());
        Address savedAddress = addressRepository.save(updatedAddress);
        return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id '" + id + "' not found"));
        addressRepository.delete(address);
    }
}
