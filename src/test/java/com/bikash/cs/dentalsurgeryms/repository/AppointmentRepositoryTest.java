package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DentistRepository dentistRepository;
    @Autowired
    private SurgeryRepository surgeryRepository;

    private Patient patient;
    private Dentist dentist;
    private Surgery surgery;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        // Minimal User for Patient and Dentist
        User patientUser = User.builder().username("MarkHill@23").password("thda#js2&").roles(List.of(Role.PATIENT)).build();
        User dentistUser = User.builder().username("DUstyRoasd34%").password("hdd8^93bhsd6gyd").roles(List.of(Role.DENTIST,Role.MANAGER)).build();

        // Minimal Address for Patient
        Address address = Address.builder().street("1 Main").city("Town").state("ST").zipCode("10000").build();

        patient = Patient.builder()
                .firstName("Alice")
                .lastName("Parker")
                .phoneNumber("1234567890")
                .email("alice@example.com")
                .hasUnpaidBill(false)
                .dateOfBirth(java.time.LocalDate.of(1992, 3, 4))
                .user(patientUser)
                .address(address)
                .build();
        patient = patientRepository.save(patient);

        dentist = Dentist.builder()
                .firstName("Dr.")
                .lastName("Dentist")
                .phoneNumber("+15550123456")
                .email("dentist@example.com")
                .specialization("General")
                .user(dentistUser)
                .build();
        dentist = dentistRepository.save(dentist);

        surgery = Surgery.builder()
                .branchCode("BR-B1")
                .surgeryName("Dental Surgery Central")
                .phoneNumber("1234567890")
                .address(address)
                .build();
        surgery = surgeryRepository.save(surgery);

        appointment = Appointment.builder()
                .appointmentDateTime(LocalDateTime.now().plusDays(1))
                .status(AppointmentStatus.SCHEDULED)
                .patient(patient)
                .dentist(dentist)
                .surgery(surgery)
                .build();
        appointment = appointmentRepository.save(appointment);
    }

    @Test
    @DisplayName("Find appointments by patient ID")
    void givenExistingPatient_whenFindByPatientId_thenReturnAppointment() {
        Page<Appointment> page = appointmentRepository.findByPatientId(patient.getId(), PageRequest.of(0, 10));
        assertFalse(page.isEmpty());
        assertEquals(patient.getId(), page.getContent().get(0).getPatient().getId());
    }

    @Test
    @DisplayName("Find appointments by patient ID when none exist")
    void givenNoAppointmentsForPatient_whenFindByPatientId_thenReturnEmptyPage() {
        User unusedUser = User.builder().username("nopatient").password("pfewfwwd").roles(List.of(Role.PATIENT)).build();
        Address address = Address.builder().street("2 Main").city("Town").state("ST").zipCode("10000").build();
        Patient noApptPatient = Patient.builder()
                .firstName("No")
                .lastName("Appointments")
                .phoneNumber("5550000000")
                .email("noappt@example.com")
                .hasUnpaidBill(false)
                .dateOfBirth(java.time.LocalDate.of(2000, 1, 1))
                .user(unusedUser)
                .address(address)
                .build();
        noApptPatient = patientRepository.save(noApptPatient);

        Page<Appointment> page = appointmentRepository.findByPatientId(noApptPatient.getId(), PageRequest.of(0, 10));
        assertTrue(page.isEmpty());
    }



    @Test
    @DisplayName("Find appointments by dentist ID")
    void givenExistingDentist_whenFindByDentistId_thenReturnAppointment() {
        Page<Appointment> page = appointmentRepository.findByDentistId(dentist.getId(), PageRequest.of(0, 10));
        assertFalse(page.isEmpty());
        assertEquals(dentist.getId(), page.getContent().get(0).getDentist().getId());
    }

    @Test
    @DisplayName("Find appointments by dentist ID when none exist")
    void givenNoAppointmentsForDentist_whenFindByDentistId_thenReturnEmptyPage() {
        // Create and save a new dentist without appointments
        User newDentistUser = User.builder()
                .username("nodentist")
                .password("strongpass123")
                .roles(List.of(Role.DENTIST))
                .build();
        Dentist noApptDentist = Dentist.builder()
                .firstName("No")
                .lastName("Dentist")
                .phoneNumber("+11111111111")
                .email("nodentist@example.com")
                .specialization("None")
                .user(newDentistUser)
                .build();
        noApptDentist = dentistRepository.save(noApptDentist);

        Page<Appointment> page = appointmentRepository.findByDentistId(noApptDentist.getId(), PageRequest.of(0, 10));
        assertTrue(page.isEmpty());
    }

    @Test
    @DisplayName("Find appointment by dentist ID, date range and status")
    void givenExistingDentist_whenFindByDentistIdAndDateRangeAndStatus_thenReturnAppointmentList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        List<Appointment> list = appointmentRepository.findByDentistIdAndAppointmentDateTimeBetweenAndStatus(
                dentist.getId(), start, end, AppointmentStatus.SCHEDULED);
        assertFalse(list.isEmpty());
        assertEquals(dentist.getId(), list.get(0).getDentist().getId());
        assertEquals(AppointmentStatus.SCHEDULED, list.get(0).getStatus());
    }

    @Test
    @DisplayName("Exists by surgery ID")
    void givenExistingSurgery_whenExistsBySurgeryId_thenReturnTrue() {
        boolean exists = appointmentRepository.existsBySurgery_Id(surgery.getId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Exists by dentist and status not")
    void givenExistingDentist_whenExistsByDentistAndStatusNot_thenReturnTrue() {
        boolean exists = appointmentRepository.existsByDentistAndStatusNot(dentist, AppointmentStatus.CANCELLED);
        assertTrue(exists);
    }

    @Test
    @DisplayName("Exists by patient and status not")
    void givenExistingPatient_whenExistsByPatientAndStatusNot_thenReturnTrue() {
        boolean exists = appointmentRepository.existsByPatientAndStatusNot(patient, AppointmentStatus.CANCELLED);
        assertTrue(exists);
    }
}