package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.security.JwtService;
import com.bikash.cs.dentalsurgeryms.dto.request.*;
import com.bikash.cs.dentalsurgeryms.dto.response.AuthenticationResponse;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.exception.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.mapper.UserMapper;
import com.bikash.cs.dentalsurgeryms.model.User;
import com.bikash.cs.dentalsurgeryms.repository.DentistRepository;
import com.bikash.cs.dentalsurgeryms.repository.PatientRepository;
import com.bikash.cs.dentalsurgeryms.repository.UserRepository;
import com.bikash.cs.dentalsurgeryms.service.AuthenticationService;
import com.bikash.cs.dentalsurgeryms.service.DentistService;
import com.bikash.cs.dentalsurgeryms.service.PatientService;
import com.bikash.cs.dentalsurgeryms.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DentistService dentistService;
    private final UserMapper userMapper;
    private final PatientService patientService;
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequestDto authenticationRequest) {
        Authentication authentication =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );
        var user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse register(RegisterRequestDto registerRequestDto) {
        

        User user = new User(
                registerRequestDto.username(),
                passwordEncoder.encode(registerRequestDto.password())
        );
        Role role = registerRequestDto.role();
        user.addRole(role);
      // save it in db
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new DuplicateResourceException("Username '" + user.getUsername() + "' is already taken");
        }
        if(patientRepository.findByEmail(registerRequestDto.email()).isPresent() ||
                dentistRepository.findByEmail(registerRequestDto.email()).isPresent()
        ){
            throw new DuplicateResourceException("Email '" + registerRequestDto.email() + "' is already taken");
        }
        User registeredUser = userRepository.save(user);
        String token = jwtService.generateToken(registeredUser);

        if(role == Role.DENTIST || role == Role.MANAGER){
            if( registerRequestDto.specialization() ==null || registerRequestDto.specialization().trim().isEmpty() ) {
                throw new NullPointerException("Specialization cannot be null or empty");
            }
            DentistResponseDto dentistResponseDto = dentistService.createDentist(new DentistRequestDto(
                    registerRequestDto.firstName(),
                    registerRequestDto.lastName(),
                    registerRequestDto.phoneNumber(),
                    registerRequestDto.email(),
                    registerRequestDto.specialization(),
                   registeredUser.getUserId()
            ));
        }else{
            if(registerRequestDto.dateOfBirth()==null ){
                throw new NullPointerException("Date of birth cannot be null");
            }
            if(!registerRequestDto.dateOfBirth().isBefore(LocalDate.now())){
                throw new IllegalArgumentException("Date of birth shuold be in the past");
            }
            PatientResponseDto patientResponseDto =
                    patientService.createPatient(new PatientRequestDto(
                            registerRequestDto.firstName(),
                            registerRequestDto.lastName(),
                            registerRequestDto.phoneNumber(),
                            registerRequestDto.email(),
                            registerRequestDto.dateOfBirth(),
                            registerRequestDto.address(),
                            registeredUser.getUserId()
                    ));
        }
        return new AuthenticationResponse(token);
    }
}
