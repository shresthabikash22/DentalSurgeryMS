package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import com.bikash.cs.dentalsurgeryms.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") 
@DataJpaTest
@ActiveProfiles("test")
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    private Patient patient;
   private  Address address;
    private  User user;

    @BeforeEach
    void setUp() {
        // Set up fresh Address and User for each test
        address = Address.builder()
                .street("123 Baker St")
                .city("London")
                .state("LN")
                .zipCode("12345")
                .build();

        user = User.builder()
                .userId(null) // let DB generate the ID
                .username("jdoekdka")
                .password("theransajd")
                .roles(List.of(Role.PATIENT))
                .build();

        patient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("555-1234")
                .email("johndoe@example.com")
                .hasUnpaidBill(false)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .user(user)
                .address(address)
                .build();
    }

    @Test
    @DisplayName("Save new patient")
    void givenValidPatient_whenSave_thenPatientIsSaved() {
        Patient saved = patientRepository.save(patient);
        assertNotNull(saved.getId());
        assertEquals(patient.getFirstName(), saved.getFirstName());
        assertEquals(patient.getUser().getUsername(), saved.getUser().getUsername());
        assertNotNull(saved.getAddress());
    }

    @Test
    @DisplayName("Find patient by ID")
    void givenExistingPatient_whenFindById_thenReturnPatient() {
        Patient saved = patientRepository.saveAndFlush(patient);
        Optional<Patient> found = patientRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getEmail(), found.get().getEmail());
    }

    @Test
    @DisplayName("Find patient by email")
    void givenExistingPatient_whenFindByEmail_thenReturnPatient() {
        Patient saved = patientRepository.saveAndFlush(patient);
        Optional<Patient> found = patientRepository.findByEmail(saved.getEmail());
        assertTrue(found.isPresent());
        assertEquals(saved.getFirstName(), found.get().getFirstName());
    }

    @Test
    @DisplayName("Find patient by userId")
    void givenExistingPatient_whenFindByUserId_thenReturnPatient() {
        Patient saved = patientRepository.saveAndFlush(patient);
        // User ID should be populated after save
        Optional<Patient> found = patientRepository.findByUser_UserId(saved.getUser().getUserId());
        assertTrue(found.isPresent());
        assertEquals(saved.getUser().getUsername(), found.get().getUser().getUsername());
    }

    @Test
    @DisplayName("Find all paginated patients")
    void givenPatients_whenFindAllPaged_thenReturnPatientsPage() {
        // Add multiple patients if needed for paging
        patientRepository.saveAndFlush(patient);

        Page<Patient> page = patientRepository.findAll(PageRequest.of(0, 5));
        assertNotNull(page);
        assertTrue(page.getTotalElements() > 0);
        assertTrue(page.getContent().stream().anyMatch(p -> p.getEmail().equals(patient.getEmail())));
    }

    @Test
    @DisplayName("Returns empty when patient not found by email")
    void givenNonExistingPatient_whenFindByEmail_thenReturnsEmpty() {
        Optional<Patient> found = patientRepository.findByEmail("notfound@example.com");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Delete patient")
    void givenExistingPatient_whenDelete_thenPatientDeleted() {
        Patient saved = patientRepository.saveAndFlush(patient);
        patientRepository.delete(saved);
        Optional<Patient> found = patientRepository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }
}
