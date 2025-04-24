package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.exception.general.DuplicateResourceException;
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

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
        if (addressRepository.findByStreet(addressRequestDto.street()).isPresent()) {
            throw new DuplicateResourceException("Street '" + addressRequestDto.street() + "' is already taken");
        }

        Address address = addressMapper.addressRequestDtoToAddress(addressRequestDto);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    @Override
    public AddressResponseDto getAddressByStreet(String street) {
        Address address = addressRepository.findByStreet(street)
                .orElseThrow(() -> new ResourceNotFoundException("Address with street '" + street + "' not found"));
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
    public AddressResponseDto updateAddress(String street, AddressRequestDto addressRequestDto) {
        Address existingAddress = addressRepository.findByStreet(street)
                .orElseThrow(() -> new ResourceNotFoundException("Address with street '" + street + "' not found"));

        if (!existingAddress.getStreet().equals(addressRequestDto.street()) &&
                addressRepository.findByStreet(addressRequestDto.street()).isPresent()) {
            throw new DuplicateResourceException("Street '" + addressRequestDto.street() + "' is already taken");
        }

        Address updatedAddress = addressMapper.addressRequestDtoToAddress(addressRequestDto);
        updatedAddress.setId (existingAddress.getId());
        Address savedAddress = addressRepository.save(updatedAddress);
        return addressMapper.addressToAddressResponseDto(savedAddress);
    }

    @Override
    public void deleteAddress(String street) {
        Address address = addressRepository.findByStreet(street)
                .orElseThrow(() -> new ResourceNotFoundException("Address with street '" + street + "' not found"));
        addressRepository.delete(address);
    }
}
