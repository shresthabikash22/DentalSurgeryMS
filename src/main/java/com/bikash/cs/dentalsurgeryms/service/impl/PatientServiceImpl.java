package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.exception.ADSIllegalStateException;
import com.bikash.cs.dentalsurgeryms.exception.AccessDeniedException;
import com.bikash.cs.dentalsurgeryms.exception.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.exception.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.AddressMapper;
import com.bikash.cs.dentalsurgeryms.mapper.PatientMapper;
import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import com.bikash.cs.dentalsurgeryms.model.User;
import com.bikash.cs.dentalsurgeryms.repository.AddressRepository;
import com.bikash.cs.dentalsurgeryms.repository.PatientRepository;
import com.bikash.cs.dentalsurgeryms.repository.UserRepository;
import com.bikash.cs.dentalsurgeryms.service.AppointmentService;
import com.bikash.cs.dentalsurgeryms.service.PatientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AppointmentService appointmentService;
    private final AddressMapper addressMapper;


    @Override
    public PatientResponseDto createPatient(@Valid PatientRequestDto patientRequestDto) {
        if (patientRepository.findById(patientRequestDto.userId()).isPresent()) {
            throw new DuplicateResourceException("Email '" + patientRequestDto.email() + "' is already taken");
        }
        if (addressRepository.findByStreet(patientRequestDto.address().street()).isPresent()) {
            throw new DuplicateResourceException("Address street '" + patientRequestDto.address().street() + "' is already taken");
        }

        // Retrieve the existing user by id
        User user = userRepository.findById(patientRequestDto.userId())
                .orElseThrow(() -> new DuplicateResourceException("User not found with id: " + patientRequestDto.userId()));

        if (patientRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new DuplicateResourceException("User already has a patient account.");
        }

        Patient patient = patientMapper.patientRequestDtoToPatient(patientRequestDto);
        patient.setUser(user); // Link the existing user

        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.patientToPatientResponseDto(savedPatient);
    }

    @Override
    public PatientResponseDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id  '" + id + "' not found"));
        return patientMapper.patientToPatientResponseDto(patient);
    }

    @Override
    public Page<PatientResponseDto> getAllPatients(int page, int pageSize, String sortDirection, String sortBy) {
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortBy
        );
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        return patientPage.map(patientMapper::patientToPatientResponseDto);
    }

    @Override
    public PatientResponseDto updatePatient(Long id,  PatientRequestDto patientRequestDto) {
        Optional<Patient> optionalPatient = patientRepository.findByEmail(patientRequestDto.email());
        if (optionalPatient.isPresent()) {
            Patient existingPatient = optionalPatient.get();
            Patient mappedPatient = patientMapper.patientRequestDtoToPatient(patientRequestDto);
            mappedPatient.setId(existingPatient.getId());

            if(!existingPatient.getEmail().equals(mappedPatient.getEmail()) &&
                    patientRepository.findByEmail(mappedPatient.getEmail()).isPresent()
            ) {
                throw new DuplicateResourceException("Email '" + existingPatient.getEmail() + "' is already taken");
            }

            Address existingAddress = existingPatient.getAddress();
            Address mappedAddress = mappedPatient.getAddress();
            if(mappedAddress.equals(existingAddress)) {
                mappedAddress.setId(existingAddress.getId());
            }

            User existingUser = existingPatient.getUser();
            User mappedUser = mappedPatient.getUser();
            if(mappedUser != null) {
                mappedUser.setUserId(existingUser.getUserId());
            }

            mappedPatient.setAddress(mappedAddress);
            Patient updatedPatient = patientRepository.save(mappedPatient);
            return patientMapper.patientToPatientResponseDto(updatedPatient);

        }
        throw new ResourceNotFoundException("Patient with email '" + patientRequestDto.email() + "' not found");

//        Patient existingPatient = patientRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Patient with id '" + id + "' not found"));
//        User existingUser = existingPatient.getUser();
//
//        if(!existingPatient.getEmail().equals(patientRequestDto.email())){
//            throw new ResourceNotFoundException("Patient with email '" + patientRequestDto.email() + "' not found");
//        }
//        // Preserve old user and address references
//        Long existingUserId = existingPatient.getUser().getUserId();
//        Long existingAddressId = existingPatient.getAddress().getId();
//
//        // Update base fields using MapStruct
//        patientMapper.updatePatientFromPatientRequestDto(patientRequestDto, existingPatient);
//
//        // Restore preserved user and address IDs to avoid new insertions
//        if (existingPatient.getUser() != null) {
//            existingPatient.getUser().setUserId(existingUserId);
//        }
//
//        if (existingPatient.getAddress() != null) {
//            existingPatient.getAddress().setId(existingAddressId);
//        }
//
//        // Save updated patient
//        Patient updatedPatient = patientRepository.save(existingPatient);
//        return patientMapper.patientToPatientResponseDto(updatedPatient);
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id '" + id + "' not found"));
        if(appointmentService.hasAppointmentsForPatientAndStatusNot(patient, AppointmentStatus.CANCELLED)){
            throw new ADSIllegalStateException("Cannot delete patient with id '" + id + "' as it has appointments.");
        }
        patientRepository.delete(patient);
    }

    @Override
    public Page<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId, UserDetails userDetails, int page, int pageSize, String sortDirection, String sortBy) {
        User patientUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with username: " + userDetails.getUsername()));
        if (!patientUser.getUsername().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("Unauthorized access to appointments");
        }
        Page<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDentistId(patientId, page, pageSize, sortDirection, sortBy);
        return appointments;

    }

}