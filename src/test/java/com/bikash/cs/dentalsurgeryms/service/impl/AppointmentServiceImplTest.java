package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.exception.InvalidOperationException;
import com.bikash.cs.dentalsurgeryms.exception.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.AppointmentMapper;
import com.bikash.cs.dentalsurgeryms.model.*;
import com.bikash.cs.dentalsurgeryms.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DentistRepository dentistRepository;
    @Mock
    private SurgeryRepository surgeryRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;
    private Address address1, address2;
    private Patient patient;
    private Dentist dentist;
    private Surgery surgery;
    private SurgeryBasicResponseDto surgeryBasicResponseDto;
    private DentistBasicResponseDto dentistBasicResponseDto;
    private Appointment appointment;
    private AppointmentRequestDto requestDto;
    private AppointmentResponseDto responseDto;

    private static final Long PATIENT_ID = 1L;
    private static final Long DENTIST_ID = 2L;
    private static final Long SURGERY_ID = 3L;
    private static final Long APPOINTMENT_ID = 4L;

    @BeforeEach
    void setUp() {
        address1 = Address.builder()
                .street("123 Baker St")
                .city("London")
                .state("LN")
                .zipCode("12345")
                .build();

        patient = Patient.builder()
                .id(PATIENT_ID)
                .firstName("John")
                .lastName("Doe")
                .email("maileMe@johndoe.com")
                .phoneNumber("1234567890")
                .address(address1)
                .appointments(new ArrayList<>())
                .hasUnpaidBill(false)
                .build();

        address2 = Address.builder()
                .street("723 Old St")
                .city("London")
                .state("LN")
                .zipCode("16732")
                .build();
        dentist = Dentist.builder()
                .id(DENTIST_ID)
                .firstName("Amanda")
                .lastName("Clarke")
                .specialization("General")
                .email("mailme@dentist.com")
                .appointments(new ArrayList<>())
                .build();

        surgery = Surgery.builder()
                .id(SURGERY_ID)
                .build();

        appointment = Appointment.builder()
                .id(APPOINTMENT_ID)
                .patient(patient)
                .dentist(dentist)
                .surgery(surgery)
                .appointmentDateTime(LocalDateTime.now())
                .status(AppointmentStatus.REQUESTED)
                .build();

        requestDto = new AppointmentRequestDto(LocalDateTime.now(), AppointmentStatus.REQUESTED, PATIENT_ID, DENTIST_ID, SURGERY_ID);

        PatientBasicResponseDto patientBasicResponseDto = new PatientBasicResponseDto(PATIENT_ID, "John", "Doe", "1234567890", "maileMe@johndoe.com");
        dentistBasicResponseDto = new DentistBasicResponseDto(DENTIST_ID,"Amanda","Clarke","General");
        surgeryBasicResponseDto = new SurgeryBasicResponseDto(SURGERY_ID,"The Smiles dental","CT-RRG","435 West St, Irving Tx");

        responseDto = new AppointmentResponseDto(APPOINTMENT_ID, LocalDateTime.now(), AppointmentStatus.REQUESTED, patientBasicResponseDto, dentistBasicResponseDto, surgeryBasicResponseDto);

    }

    @Test
    @DisplayName("Create appointment successfully when patient, dentist, and surgery exist and limits are not hit")
    void createAppointment_success() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(dentistRepository.findById(DENTIST_ID)).thenReturn(Optional.of(dentist));
        when(appointmentRepository.findByDentistIdAndAppointmentDateTimeBetweenAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(new ArrayList<>());
        when(surgeryRepository.findById(SURGERY_ID)).thenReturn(Optional.of(surgery));
        when(appointmentMapper.appointmentRequestDtoToAppointment(any())).thenReturn(appointment);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(responseDto);

        AppointmentResponseDto result = appointmentService.createAppointment(requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(patientRepository).save(patient);
        verify(dentistRepository).save(dentist);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    @DisplayName("Create fails if patient does not exist")
    void createAppointment_patientNotFound() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.createAppointment(requestDto));

        assertThat(ex.getMessage()).contains("Patient with ID " + PATIENT_ID + " not found");
    }

    @Test
    @DisplayName("Create fails if patient has unpaid bill")
    void createAppointment_patientHasUnpaidBill() {
        patient.setHasUnpaidBill(true);
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));

        InvalidOperationException ex = assertThrows(InvalidOperationException.class,
                () -> appointmentService.createAppointment(requestDto));

        assertThat(ex.getMessage()).contains("Patient has unpaid bill");
    }

    @Test
    @DisplayName("Create fails if dentist does not exist")
    void createAppointment_dentistNotFound() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(dentistRepository.findById(DENTIST_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.createAppointment(requestDto));

        assertThat(ex.getMessage()).contains("Dentist with ID " + DENTIST_ID + " not found");
    }

    @Test
    @DisplayName("Create fails if appointment limit per week reached")
    void createAppointment_dentistAppointmentsLimitReached() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(dentistRepository.findById(DENTIST_ID)).thenReturn(Optional.of(dentist));
        List<Appointment> fullList = List.of(new Appointment(), new Appointment(), new Appointment(), new Appointment(), new Appointment());
        when(appointmentRepository.findByDentistIdAndAppointmentDateTimeBetweenAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(fullList);

        InvalidOperationException ex = assertThrows(InvalidOperationException.class,
                () -> appointmentService.createAppointment(requestDto));

        assertThat(ex.getMessage()).contains("Dentist cannot have more then 5 appointments in a week");
    }

    @Test
    @DisplayName("Create fails if surgery does not exist")
    void createAppointment_surgeryNotFound() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(dentistRepository.findById(DENTIST_ID)).thenReturn(Optional.of(dentist));
        when(appointmentRepository.findByDentistIdAndAppointmentDateTimeBetweenAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(new ArrayList<>());
        when(surgeryRepository.findById(SURGERY_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.createAppointment(requestDto));

        assertThat(ex.getMessage()).contains("Surgery with ID " + SURGERY_ID + " not found");
    }

    @Test
    @DisplayName("Get appointment by ID - success")
    void getAppointmentById_success() {
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.appointmentToAppointmentResponseDto(appointment)).thenReturn(responseDto);

        AppointmentResponseDto dto = appointmentService.getAppointmentById(APPOINTMENT_ID);

        assertThat(dto).isEqualTo(responseDto);
        verify(appointmentRepository).findById(APPOINTMENT_ID);
    }

    @Test
    @DisplayName("Get appointment by ID - not found")
    void getAppointmentById_notFound() {
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getAppointmentById(APPOINTMENT_ID));

        assertThat(ex.getMessage()).contains("Appointment with ID " + APPOINTMENT_ID + " not found");
    }

    @Test
    @DisplayName("Get appointments by patient - success")
    void getAppointmentsByPatientId_success() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, "appointmentDateTime");
        Page<Appointment> page = new PageImpl<>(List.of(appointment));
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientId(eq(PATIENT_ID), any(Pageable.class))).thenReturn(page);
        when(appointmentMapper.appointmentToAppointmentResponseDto(appointment)).thenReturn(responseDto);

        Page<AppointmentResponseDto> result = appointmentService.getAppointmentsByPatientId(
                PATIENT_ID, 0, 10, "ASC", "appointmentDateTime");

        assertThat(result.getContent()).containsExactly(responseDto);
    }

    @Test
    @DisplayName("Get appointments by patient - patient not found")
    void getAppointmentsByPatientId_patientNotFound() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getAppointmentsByPatientId(
                        PATIENT_ID, 0, 10, "ASC", "appointmentDateTime"));

        assertThat(ex.getMessage()).contains("Patient with ID " + PATIENT_ID + " not found");
    }

    @Test
    @DisplayName("Get appointments by dentist - success")
    void getAppointmentsByDentistId_success() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, "appointmentDateTime");
        Page<Appointment> page = new PageImpl<>(List.of(appointment));
        when(dentistRepository.findById(DENTIST_ID)).thenReturn(Optional.of(dentist));
        when(appointmentRepository.findByDentistId(eq(DENTIST_ID), any(Pageable.class))).thenReturn(page);
        when(appointmentMapper.appointmentToAppointmentResponseDto(appointment)).thenReturn(responseDto);

        Page<AppointmentResponseDto> result = appointmentService.getAppointmentsByDentistId(
                DENTIST_ID, 0, 10, "ASC", "appointmentDateTime");

        assertThat(result.getContent()).containsExactly(responseDto);
    }

    @Test
    @DisplayName("Get appointments by dentist - dentist not found")
    void getAppointmentsByDentistId_dentistNotFound() {
        when(dentistRepository.findById(DENTIST_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getAppointmentsByDentistId(
                        DENTIST_ID, 0, 10, "ASC", "appointmentDateTime"));
        assertThat(ex.getMessage()).contains("Dentist with ID " + DENTIST_ID + " not found");
    }

    @Test
    @DisplayName("Cancel appointment - success")
    void cancelAppointment_success() {
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(responseDto);

        AppointmentResponseDto cancelled = appointmentService.cancelAppointment(APPOINTMENT_ID);

        assertThat(cancelled).isEqualTo(responseDto);
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    @DisplayName("Cancel appointment - already cancelled")
    void cancelAppointment_alreadyCancelled() {
        appointment.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        InvalidOperationException ex = assertThrows(InvalidOperationException.class,
                () -> appointmentService.cancelAppointment(APPOINTMENT_ID));
        assertThat(ex.getMessage()).contains("already cancelled");
    }

    @Test
    @DisplayName("Update appointment - success")
    void updateAppointment_success() {
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(dentistRepository.findById(DENTIST_ID)).thenReturn(Optional.of(dentist));
        when(surgeryRepository.findById(SURGERY_ID)).thenReturn(Optional.of(surgery));
        when(appointmentRepository.findByDentistIdAndAppointmentDateTimeBetweenAndStatus(anyLong(), any(), any(), any()))
                .thenReturn(new ArrayList<>());
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.appointmentToAppointmentResponseDto(any())).thenReturn(responseDto);

        AppointmentResponseDto updated = appointmentService.updateAppointment(APPOINTMENT_ID, requestDto);

        assertThat(updated).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Update appointment - patient has unpaid bill")
    void updateAppointment_patientUnpaidBill() {
        patient.setHasUnpaidBill(true);
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));

        InvalidOperationException ex = assertThrows(InvalidOperationException.class,
                () -> appointmentService.updateAppointment(APPOINTMENT_ID, requestDto));
        assertThat(ex.getMessage()).contains("unpaid bill");
    }

    @Test
    @DisplayName("Delete appointment - success")
    void deleteAppointment_success() {
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointment(APPOINTMENT_ID);

        verify(appointmentRepository).delete(appointment);
    }

    @Test
    @DisplayName("Delete appointment - not found")
    void deleteAppointment_notFound() {
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.deleteAppointment(APPOINTMENT_ID));
        assertThat(ex.getMessage()).contains("Appointment with ID " + APPOINTMENT_ID + " not found");
    }

    @Test
    @DisplayName("Has appointments for surgery")
    void hasAppointmentsForSurgery() {
        when(appointmentRepository.existsBySurgery_Id(SURGERY_ID)).thenReturn(true);
        assertTrue(appointmentService.hasAppointmentsForSurgery(SURGERY_ID));
        verify(appointmentRepository).existsBySurgery_Id(SURGERY_ID);
    }

    @Test
    @DisplayName("Has appointments for dentist and status not")
    void hasAppointmentsForDentistAndStatusNot() {
        when(appointmentRepository.existsByDentistAndStatusNot(dentist, AppointmentStatus.COMPLETED)).thenReturn(true);
        assertTrue(appointmentService.hasAppointmentsForDentistAndStatusNot(dentist, AppointmentStatus.COMPLETED));
    }

    @Test
    @DisplayName("Has appointments for patient and status not")
    void hasAppointmentsForPatientAndStatusNot() {
        when(appointmentRepository.existsByPatientAndStatusNot(patient, AppointmentStatus.CANCELLED)).thenReturn(false);
        assertFalse(appointmentService.hasAppointmentsForPatientAndStatusNot(patient, AppointmentStatus.CANCELLED));
    }
}