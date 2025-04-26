package com.bikash.cs.dentalsurgeryms.service.impl;


import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.exception.ADSIllegalStateException;
import com.bikash.cs.dentalsurgeryms.exception.AccessDeniedException;
import com.bikash.cs.dentalsurgeryms.exception.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.exception.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.AppointmentMapper;
import com.bikash.cs.dentalsurgeryms.mapper.DentistMapper;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
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
import org.springframework.security.core.userdetails.UserDetails;
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
    private final AppointmentMapper appointmentMapper;

    @Override
    public DentistResponseDto createDentist(@Valid DentistRequestDto dentistRequestDto) {
        if (dentistRepository.findByEmail(dentistRequestDto.email()).isPresent()) {
            throw new DuplicateResourceException("Email '" + dentistRequestDto.email() + "' is already taken");
        }
        Dentist dentist = dentistMapper.dentistRequestDtoToDentist(dentistRequestDto);

        Optional<User> user = userRepository.findById(dentistRequestDto.userId());
        User savedUser;
        if (user.isPresent()){
            savedUser = user.get();
            if(dentistRepository.findByUser_UserId(user.get().getUserId()).isPresent() ){
                throw new DuplicateResourceException("User already has a dentist account");
            }
            dentist.setUser(savedUser);
        }

        Dentist savedDentist = dentistRepository.save(dentist);
        return dentistMapper.dentistToDentistResponseDto(savedDentist);
    }

    @Override
    public DentistResponseDto getDentistById(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist with id '" + id + "' not found"));
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
    public DentistResponseDto updateDentist(Long id, @Valid DentistRequestDto dentistRequestDto) {

        Dentist existingDentist = dentistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist with id '" + id + "' not found "));
        Long existingUserId = existingDentist.getUser().getUserId();

        dentistMapper.updateDentistFromDentistRequestDto(dentistRequestDto, existingDentist);
        if (existingDentist.getUser() != null) {
            existingDentist.getUser().setUserId(existingUserId);
        } else {
            User user = new User();
            existingDentist.setUser(user);
        }

        Dentist savedDentist = dentistRepository.save(existingDentist);
        return dentistMapper.dentistToDentistResponseDto(savedDentist);
    }

    @Override
    public void deleteDentist(Long id, Role role) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist with id '" + id + "' not found"));
        if(role == Role.DENTIST){
            if (appointmentService.hasAppointmentsForDentistAndStatusNot(dentist, AppointmentStatus.CANCELLED)) {
                throw new ADSIllegalStateException("Dentist has appointments in the future. Cannot delete.");
            }
            dentistRepository.delete(dentist);
        }
        else{
            dentist.getUser().removeRole(Role.MANAGER);
            dentistRepository.save(dentist);
        }

    }

//    @Override
    public Page<AppointmentResponseDto> getAppointmentsByDentist(Long dentistId, UserDetails userDetails, int page, int pageSize, String sortDirection, String sortBy) {
        User dentistUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found with username: " + userDetails.getUsername()));
        if (! dentistUser.getUsername().equals(userDetails.getUsername()) ){
            throw new AccessDeniedException("Unauthorized access to appointments");
        }
        Page<AppointmentResponseDto> appointments = appointmentService.getAppointmentsByDentistId(dentistId, page,pageSize, sortDirection, sortBy);
        return appointments;
    }

}
