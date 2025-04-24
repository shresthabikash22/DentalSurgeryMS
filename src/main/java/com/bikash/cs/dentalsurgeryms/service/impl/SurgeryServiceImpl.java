package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.request.SurgeryRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.exception.general.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.exception.general.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.SurgeryMapper;
import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import com.bikash.cs.dentalsurgeryms.repository.AddressRepository;
import com.bikash.cs.dentalsurgeryms.repository.SurgeryRepository;
import com.bikash.cs.dentalsurgeryms.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SurgeryServiceImpl implements SurgeryService {

    private final SurgeryRepository surgeryRepository;
    private final AddressRepository addressRepository;
    private final SurgeryMapper surgeryMapper;

    @Override
    public SurgeryResponseDto createSurgery(SurgeryRequestDto surgeryRequestDto) {
        if (surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode()).isPresent()) {
            throw new DuplicateResourceException("Surgery with branch code '" + surgeryRequestDto.branchCode() + "' already exists.");
        }
        if (addressRepository.findByStreet(surgeryRequestDto.addressRequestDto().street()).isPresent()) {
            throw new DuplicateResourceException("Address with street '" + surgeryRequestDto.addressRequestDto().street() + "' is already taken.");
        }

        Surgery surgery = surgeryMapper.surgeryRequestDtoToSurgery(surgeryRequestDto);
        Surgery savedSurgery = surgeryRepository.save(surgery);
        return surgeryMapper.surgeryToSurgeryResponseDto(savedSurgery);
    }

    @Override
    public SurgeryResponseDto getSurgeryByBranchCode(String branchCode) {
        Surgery surgery = surgeryRepository.findByBranchCode(branchCode)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery with branch code '" + branchCode + "' not found"));
        return surgeryMapper.surgeryToSurgeryResponseDto(surgery);
    }


    @Override
    public Page<SurgeryResponseDto> getAllSurgeries(int page, int pageSize, String sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortBy
        );
        Page<Surgery> surgeriesPage = surgeryRepository.findAll(pageable);
        return surgeriesPage.map(surgeryMapper::surgeryToSurgeryResponseDto);
    }

    @Override
    public SurgeryResponseDto updateSurgery(String branchCode, SurgeryRequestDto surgeryRequestDto) {
        Optional<Surgery> optionalSurgery = surgeryRepository.findByBranchCode(branchCode);
        if(optionalSurgery.isPresent()){
            Surgery existingSurgery = optionalSurgery.get();
            Surgery mappedSurgery = surgeryMapper.surgeryRequestDtoToSurgery(surgeryRequestDto);
            mappedSurgery.setId(existingSurgery.getId());
            //checking branchCode
            if(!existingSurgery.getBranchCode().equals(mappedSurgery.getBranchCode()) &&
                    surgeryRepository.findByBranchCode(mappedSurgery.getBranchCode()).isPresent()
            ){
                throw new DuplicateResourceException("Branch Code: "+ mappedSurgery.getBranchCode() + "is already taken.");
            }
            //checking Address
            Address existingAddress = existingSurgery.getAddress();
            Address mappedAddress = mappedSurgery.getAddress();
            // address we got/mapped from surgeryRequestDto
            if(mappedAddress!=null){
                if(!mappedAddress.getStreet().equals(existingAddress.getStreet())
                && addressRepository.findByStreet(mappedAddress.getStreet()).isPresent()){
                    throw new DuplicateResourceException("Address with street '" + mappedAddress.getStreet() + "' is already taken.");
                }mappedAddress.setId(existingAddress.getId());
            }
            mappedSurgery.setAddress(mappedAddress);
            Surgery savedSurgery = surgeryRepository.save(mappedSurgery);
            return surgeryMapper.surgeryToSurgeryResponseDto(savedSurgery);


        }
        throw new ResourceNotFoundException("Surgery with code '" + branchCode + "' not found.");
    }

    @Override
    public void deleteSurgery(String branchCode) {
        Surgery surgery = surgeryRepository.findByBranchCode(branchCode)
                .orElseThrow(() -> new ResourceNotFoundException("Surgery with branch code '" + branchCode + "' not found"));
        surgeryRepository.delete(surgery);
    }
}
