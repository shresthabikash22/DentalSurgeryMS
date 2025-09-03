package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.enums.Role;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test") 
@DataJpaTest
@ActiveProfiles("test")
class DentistRepositoryTest {

    @Autowired
    private DentistRepository dentistRepository;

    private Dentist dentist;
    User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .userId(null)
                .username("jsmith")
                .password("password123")
                .roles(List.of(Role.DENTIST, Role.MANAGER))
                .build();

        dentist = com.bikash.cs.dentalsurgeryms.model.Dentist.builder()
                .firstName("John")
                .lastName("Smith")
                .phoneNumber("+1234567890")
                .email("john.smith@clinic.com")
                .specialization("General Dentistry")
                .user(user)
                .build();
    }

    @Test
    @DisplayName("Save new dentist")
    void givenValidDentist_whenSave_thenDentistIsSaved() {
        Dentist saved = dentistRepository.save(dentist);
        assertNotNull(saved.getId());
        assertEquals(dentist.getFirstName(), saved.getFirstName());
        assertEquals(dentist.getUser().getUsername(), saved.getUser().getUsername());
    }

    @Test
    @DisplayName("Find dentist by email")
    void givenExistingDentist_whenFindByEmail_thenReturnDentist() {
        Dentist saved = dentistRepository.saveAndFlush(dentist);
        Optional<Dentist> found = dentistRepository.findByEmail(saved.getEmail());
        assertTrue(found.isPresent());
        assertEquals(saved.getLastName(), found.get().getLastName());
    }

    @Test
    @DisplayName("Find dentist by user ID")
    void givenExistingDentist_whenFindByUserId_thenReturnDentist() {
        Dentist saved = dentistRepository.saveAndFlush(dentist);
        Optional<Dentist> found = dentistRepository.findByUser_UserId(saved.getUser().getUserId());
        assertTrue(found.isPresent());
        assertEquals(saved.getEmail(), found.get().getEmail());
    }

    @Test
    @DisplayName("Find all paginated dentists")
    void givenDentists_whenFindAllPaged_thenReturnDentistsPage() {
        dentistRepository.saveAndFlush(dentist);

        Page<Dentist> page = dentistRepository.findAll(PageRequest.of(0, 5));
        assertNotNull(page);
        assertTrue(page.getTotalElements() > 0);
        assertTrue(page.getContent().stream().anyMatch(d -> d.getEmail().equals(dentist.getEmail())));
    }

    @Test
    @DisplayName("Returns empty when dentist not found by email")
    void givenNonExistingDentist_whenFindByEmail_thenReturnsEmpty() {
        Optional<Dentist> found = dentistRepository.findByEmail("notfound@clinic.com");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Delete dentist")
    void givenExistingDentist_whenDelete_thenDentistDeleted() {
        Dentist saved = dentistRepository.saveAndFlush(dentist);
        dentistRepository.delete(saved);
        Optional<Dentist> found = dentistRepository.findById(saved.getId());
        assertTrue(found.isEmpty());
    }
}
