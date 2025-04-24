package com.bikash.cs.dentalsurgeryms.service.impl;


import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.exception.general.ADSIllegalStateException;
import com.bikash.cs.dentalsurgeryms.exception.general.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.exception.general.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.DentistMapper;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.User;
import com.bikash.cs.dentalsurgeryms.repository.DentistRepository;
import com.bikash.cs.dentalsurgeryms.repository.UserRepository;
import com.bikash.cs.dentalsurgeryms.service.AppointmentService;
import com.bikash.cs.dentalsurgeryms.service.DentistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {
    private final DentistRepository dentistRepository;
    private final DentistMapper dentistMapper;
    private final UserRepository userRepository;
    private final AppointmentService appointmentService;

    @Override
    public DentistResponseDto createDentist(@Valid DentistRequestDto dentistRequestDto) {
        if (dentistRepository.findByEmail(dentistRequestDto.email()).isPresent()) {
            throw new DuplicateResourceException("Email '" + dentistRequestDto.email() + "' is already taken");
        }
        Optional<User>  user = userRepository.findByUsername(dentistRequestDto.user().username());
        if(user.isPresent() && dentistRepository.findByUser_UserId(user.get().getUserId()).isPresent() ) {
            throw new DuplicateResourceException("User already has a dentist account");
        }

        Dentist dentist = dentistMapper.dentistRequestDtoToDentist(dentistRequestDto);
        Dentist savedDentist = dentistRepository.save(dentist);
        return dentistMapper.dentistToDentistResponseDto(savedDentist);
    }

    @Override
    public DentistResponseDto getDentistByEmail(String email) {
        Dentist dentist = dentistRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist with email '" + email + "' not found"));
        return dentistMapper.dentistToDentistResponseDto(dentist);
    }

    @Override
    public Page<DentistResponseDto> getAllDentists(int page, int pageSize, String sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortBy
        );
        Page<Dentist> dentistsPage = dentistRepository.findAll(pageable);
        return dentistsPage.map(dentistMapper::dentistToDentistResponseDto);
    }

    @Override
    public DentistResponseDto updateDentist(String email,@Valid DentistRequestDto dentistRequestDto) {
//        Optional<Dentist> optionalDentist = dentistRepository.findByEmail(email);
//        if (optionalDentist.isPresent()) {
//            Dentist existingDentist = optionalDentist.get();
//            Dentist mappedDentist = dentistMapper.dentistRequestDtoToDentist(dentistRequestDto);
//            if(!existingDentist.getEmail().equals(dentistRequestDto.email()) &&
//                    dentistRepository.findByEmail(mappedDentist.getEmail()).isPresent()
//            ){
//                throw new DuplicateResourceException("Email '" + dentistRequestDto.email() + "' already exists");
//            }
//            mappedDentist.setId(existingDentist.getId());
//            Dentist updatedDentist = dentistRepository.save(mappedDentist);
//            return dentistMapper.dentistToDentistResponseDto(updatedDentist);
//
//        }
//       throw new ResourceNotFoundException("Dentist with email '" + email + "' not found");
        Dentist existingDentist = dentistRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Dentist with email '" + email + "' not found "));
        Long existingUserId = existingDentist.getUser().getUserId();

        dentistMapper.updateDentistFromDentistRequestDto(dentistRequestDto,existingDentist);
        if(existingDentist.getUser()!=null){
            existingDentist.getUser().setUserId(existingUserId);
        }else{
            User  user= new User();
            existingDentist.setUser(user);
        }

        Dentist savedDentist = dentistRepository.save(existingDentist);
        return dentistMapper.dentistToDentistResponseDto(savedDentist);
    }

    @Override
    public void deleteDentist(String email) {
        Dentist dentist = dentistRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist with email '" + email + "' not found"));
        if(appointmentService.hasAppointmentsForDentistAndStatusNot(dentist, AppointmentStatus.CANCELLED)){
            throw new ADSIllegalStateException("Dentist has appointments in the future. Cannot delete.");
        }
        dentistRepository.delete(dentist);
    }
}
