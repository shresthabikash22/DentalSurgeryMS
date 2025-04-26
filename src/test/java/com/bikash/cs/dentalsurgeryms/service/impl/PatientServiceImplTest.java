package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
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
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock PatientRepository patientRepository;
    @Mock PatientMapper patientMapper;
    @Mock AddressRepository addressRepository;
    @Mock UserRepository userRepository;
    @Mock AppointmentService appointmentService;
    @Mock AddressMapper addressMapper;
    @InjectMocks PatientServiceImpl patientService;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    @DisplayName("create a new patient")
    void givenNonExisting_whenCreate_thenReturnsPatientResponseDto() {
        PatientRequestDto req = buildPatientRequestDto(100L, "john@mail.com", "Alder St");
        User user = new User(); user.setUserId(100L);
        Patient patient = Patient.builder().user(user).email("john@mail.com").build();
        Patient savedPatient = Patient.builder().user(user).email("john@mail.com").build();
        PatientResponseDto resp = mock(PatientResponseDto.class);

        when(patientRepository.findById(100L)).thenReturn(Optional.empty());
        when(addressRepository.findByStreet("Alder St")).thenReturn(Optional.empty());
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(patientRepository.findByUser_UserId(100L)).thenReturn(Optional.empty());
        when(patientMapper.patientRequestDtoToPatient(req)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientMapper.patientToPatientResponseDto(savedPatient)).thenReturn(resp);

        assertEquals(resp, patientService.createPatient(req));
    }

    @Test
    @DisplayName("create existing patient")
    void givenExistingUser_whenCreatePatient_thenThrowDuplicateResourceException() {
        PatientRequestDto req = buildPatientRequestDto(100L, "duplicate@mail.com", "Alder St");
        when(patientRepository.findById(100L)).thenReturn(Optional.of(mock(Patient.class)));
        assertThrows(DuplicateResourceException.class, () -> patientService.createPatient(req));
    }



    @Test
    @DisplayName("craeting a new patient for existing user")
    void givenNonExistingUser_whenCreatePatient_thenThrows() {
        PatientRequestDto req = buildPatientRequestDto(100L, "another@mail.com", "Other St");
        when(patientRepository.findById(100L)).thenReturn(Optional.empty());
        when(addressRepository.findByStreet("Other St")).thenReturn(Optional.empty());
        when(userRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.createPatient(req));
    }

    @Test
    @DisplayName("given existing patient user in createPatient then throws DuplicateResourceException")
    void givenExistingPatientUser_whenCreatePatient_thenThrows() {
        PatientRequestDto req = buildPatientRequestDto(100L, "john@mail.com", "Elm St");
        User user = new User(); user.setUserId(100L);
        when(patientRepository.findById(100L)).thenReturn(Optional.empty());
        when(addressRepository.findByStreet("Elm St")).thenReturn(Optional.empty());
        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(patientRepository.findByUser_UserId(100L)).thenReturn(Optional.of(mock(Patient.class)));
        assertThrows(DuplicateResourceException.class, () -> patientService.createPatient(req));
    }

    @Test
    @DisplayName("given valid id when getPatientById then returns PatientResponseDto")
    void givenValidId_whenGetPatientById_thenReturnsResponse() {
        Patient p = Patient.builder().id(1L).build();
        PatientResponseDto resp = mock(PatientResponseDto.class);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(p));
        when(patientMapper.patientToPatientResponseDto(p)).thenReturn(resp);
        assertEquals(resp, patientService.getPatientById(1L));
    }

    @Test
    @DisplayName("given invalid id when getPatientById then throws ResourceNotFoundException")
    void givenInvalidId_whenGetPatientById_thenThrows() {
        when(patientRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(42L));
    }

    @Test
    @DisplayName("given valid paging when getAllPatients then returns paged response")
    void givenValidPaging_whenGetAllPatients_thenReturnsPage() {
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.DESC, "id");
        Patient p = Patient.builder().id(1L).build();
        PatientResponseDto responseDto = mock(PatientResponseDto.class);
        List<Patient> patients = List.of(p);
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, 1);
        when(patientRepository.findAll(pageable)).thenReturn(patientPage);
        when(patientMapper.patientToPatientResponseDto(p)).thenReturn(responseDto);

        Page<PatientResponseDto> page = patientService.getAllPatients(0, 2, "DESC", "id");
        assertEquals(1, page.getTotalElements());
        assertEquals(responseDto, page.getContent().get(0));
    }

    @Test
    @DisplayName("given valid id and PatientRequestDto when updatePatient then returns PatientResponseDto")
    void givenValidPatient_whenUpdatePatient_thenReturnsResponseDto() {
        Long patientId = 9L;
        PatientRequestDto requestDto = buildPatientRequestDto(11L, "new@mail.com", "Some St");
        Patient existing = Patient.builder()
            .id(patientId)
            .email("old@mail.com")
            .address(Address.builder().id(10L).street("Some St").build())
            .user(new User())
            .build();
        Patient mapped = Patient.builder()
            .id(patientId)
            .email("new@mail.com")
            .address(Address.builder().id(10L).street("Some St").build())
            .user(new User())
            .build();
        PatientResponseDto resp = mock(PatientResponseDto.class);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existing));
        when(patientMapper.patientRequestDtoToPatient(requestDto)).thenReturn(mapped);
        when(patientRepository.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(patientRepository.save(mapped)).thenReturn(mapped);
        when(patientMapper.patientToPatientResponseDto(mapped)).thenReturn(resp);

        assertEquals(resp, patientService.updatePatient(patientId, requestDto));
    }

    @Test
    @DisplayName("given patient not found in updatePatient then throws ResourceNotFoundException")
    void givenNotFound_whenUpdatePatient_thenThrows() {
        PatientRequestDto req = buildPatientRequestDto(1L, "test@mail.com", "X");
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(2L, req));
    }

    @Test
    @DisplayName("given duplicate email in updatePatient then throws DuplicateResourceException")
    void givenDuplicateEmail_whenUpdatePatient_thenThrows() {
        Long patientId = 1L;
        PatientRequestDto req = buildPatientRequestDto(2L, "taken@mail.com", "Y St");
        Patient existing = Patient.builder()
            .id(patientId)
            .email("old@mail.com")
            .address(Address.builder().street("Y St").build())
            .user(new User())
            .build();
        Patient mapped = Patient.builder()
            .id(patientId)
            .email("taken@mail.com")
            .address(Address.builder().street("Y St").build())
            .user(new User())
            .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existing));
        when(patientMapper.patientRequestDtoToPatient(req)).thenReturn(mapped);
        when(patientRepository.findByEmail("taken@mail.com")).thenReturn(Optional.of(mock(Patient.class)));

        assertThrows(DuplicateResourceException.class, () -> patientService.updatePatient(patientId, req));
    }

    @Test
    @DisplayName("given valid id with no appointments when deletePatient then deleted successfully")
    void givenValidIdAndNoAppointments_whenDeletePatient_thenDeletes() {
        Patient p = Patient.builder().id(100L).build();
        when(patientRepository.findById(100L)).thenReturn(Optional.of(p));
        when(appointmentService.hasAppointmentsForPatientAndStatusNot(p, AppointmentStatus.CANCELLED)).thenReturn(false);
        patientService.deletePatient(100L);
        verify(patientRepository).delete(p);
    }

    @Test
    @DisplayName("given patient not found in deletePatient then throws ResourceNotFoundException")
    void givenNotFound_whenDeletePatient_thenThrows() {
        when(patientRepository.findById(404L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatient(404L));
    }

    @Test
    @DisplayName("given patient with appointments not canceled when deletePatient then throws ADSIllegalStateException")
    void givenAppointmentsExist_whenDeletePatient_thenThrows() {
        Patient p = Patient.builder().id(100L).build();
        when(patientRepository.findById(100L)).thenReturn(Optional.of(p));
        when(appointmentService.hasAppointmentsForPatientAndStatusNot(p, AppointmentStatus.CANCELLED)).thenReturn(true);
        assertThrows(ADSIllegalStateException.class, () -> patientService.deletePatient(100L));
    }

    @Test
    @DisplayName("given valid userDetails and patientId when getAppointmentsByPatientId then returns appointments page")
    void givenValidUserAndId_whenGetAppointmentsByPatientId_thenPageReturned() {
        Long patientId = 10L;
        User user = new User(); user.setUsername("usr1");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("usr1");
        when(userRepository.findByUsername("usr1")).thenReturn(Optional.of(user));
        AppointmentResponseDto apptResp = mock(AppointmentResponseDto.class);
        Page<AppointmentResponseDto> page = new PageImpl<>(List.of(apptResp));
        when(appointmentService.getAppointmentsByDentistId(patientId, 0, 10, "ASC", "id")).thenReturn(page);

        Page<AppointmentResponseDto> result = patientService.getAppointmentsByPatientId(patientId, userDetails, 0, 10, "ASC", "id");
        assertEquals(1, result.getTotalElements());
        assertEquals(apptResp, result.getContent().get(0));
    }

    @Test
    @DisplayName("given not found user when getAppointmentsByPatientId then throws ResourceNotFoundException")
    void givenNotFoundUser_whenGetAppointmentsByPatientId_thenThrows() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("usrX");
        when(userRepository.findByUsername("usrX")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.getAppointmentsByPatientId(1L, userDetails, 0, 5, "ASC", "id"));
    }

    @Test
    @DisplayName("given mismatched username when getAppointmentsByPatientId then throws AccessDeniedException")
    void givenMismatchedUsername_whenGetAppointmentsByPatientId_thenThrows() {
        User user = new User(); user.setUsername("userA");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("userB");
        when(userRepository.findByUsername("userB")).thenReturn(Optional.of(user));
        assertThrows(AccessDeniedException.class, () -> patientService.getAppointmentsByPatientId(1L, userDetails, 0, 5, "ASC", "id"));
    }

    // Helper builder
    private PatientRequestDto buildPatientRequestDto(Long userId, String email, String street) {
        return new PatientRequestDto(
                "First", "Last", "+1234567890", email, LocalDate.of(1999, 1, 1),
                new AddressRequestDto(street, "City", "State", "12345"),
                userId
        );
    }
}