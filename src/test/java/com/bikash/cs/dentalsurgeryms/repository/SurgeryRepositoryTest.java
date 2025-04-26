package com.bikash.cs.dentalsurgeryms.repository;

import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class SurgeryRepositoryTest {
    @Autowired
    private SurgeryRepository surgeryRepository;
    @Autowired
    private AddressRepository addressRepository;

    private Surgery surgery, surgery1;
    private Address address, address1;


    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        address = Address.builder().street("476 South St").city("New York").state("NY").zipCode("10001").build();
        address1 = Address.builder().street("47 college road").city("New York").state("NY").zipCode("10002").build();

        surgery = Surgery.builder()
                .branchCode("BR-B1")
                .surgeryName("Dental Surgery Central")
                .phoneNumber("1234567890")
                .address(address)
                .build();


        surgery1 = Surgery.builder()
                .branchCode("BR-002")
                .surgeryName("Dental Surgery West")
                .address(address1)
                .phoneNumber("0987654321")
                .build();
    }

    @Test
    @DisplayName("test for saving new surgery")
    void givenNonExistingSurgery_whenCreateSurgery_thenSurgerySaved() {
        Surgery savedSurgery = surgeryRepository.saveAndFlush(surgery);
        assertNotNull(savedSurgery);
        assertEquals(surgery.getBranchCode(), savedSurgery.getBranchCode());
        assertEquals(surgery.getSurgeryName(), savedSurgery.getSurgeryName());
        assertEquals(surgery.getAddress(), savedSurgery.getAddress());
    }

    @Test
    @DisplayName("test for finding surgery by branchCode")
    void givenExistingSurgery_whenFindByBranchCode_thenReturnSurgery() {
        Surgery savedSurgery = surgeryRepository.saveAndFlush(surgery);
        Surgery found = surgeryRepository.findByBranchCode(savedSurgery.getBranchCode()).orElse(null);
        assertNotNull(found);
        assertEquals(savedSurgery.getBranchCode(), found.getBranchCode());
        assertEquals(savedSurgery.getSurgeryName(), found.getSurgeryName());
    }

    @Test
    @DisplayName("test for finding surgery by non-existent branchCode")
    void givenNonExistingSurgery_whenFindByBranchCode_thenReturnEmpty() {
        Optional<Surgery> found = surgeryRepository.findByBranchCode("ZZ999");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("test for finding all surgeries")
    void givenSurgeries_whenFindAll_thenReturnList() {
        Surgery savedSurgery = surgeryRepository.saveAndFlush(surgery);
        List<Surgery> foundSurgeries = surgeryRepository.findAll();
        assertNotNull(foundSurgeries);
        assertTrue(foundSurgeries.stream().anyMatch(s -> s.getBranchCode().equals(savedSurgery.getBranchCode())));
    }

    @Test
    @DisplayName("test for deleting surgery")
    void givenExistingSurgery_whenDelete_thenSurgeryDeleted() {
        Surgery savedSurgery = surgeryRepository.saveAndFlush(surgery);
        surgeryRepository.delete(savedSurgery);
        Optional<Surgery> found = surgeryRepository.findByBranchCode(savedSurgery.getBranchCode());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("find paginated list of surgeries")
    void givenExistingSurgeries_whenFindAll_thenReturnPaginatedSurgeryList() {
        surgeryRepository.saveAllAndFlush(List.of(surgery, surgery1));
        Pageable pageable = PageRequest.of(0, 2, Sort.by("branchCode").ascending());
        Page<Surgery> page = surgeryRepository.findAll(pageable);
        assertNotNull(page);
        assertEquals(2, page.getNumberOfElements());
        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getTotalPages());
        assertEquals(0, page.getNumber());
        assertFalse(page.hasNext());
        assertFalse(page.hasPrevious());
    }

    @Test
    @DisplayName("findAll returns empty page when no surgeries exist")
    void givenNonExistingSurgeries_whenFindAll_thenReturnsNoSurgeries() {
        Pageable pageable = PageRequest.of(1, 2, Sort.by("branchCode").ascending());
        Page<Surgery> surgeryPage = surgeryRepository.findAll(pageable);

        assertNotNull(surgeryPage);
        assertEquals(0, surgeryPage.getNumberOfElements());
        assertEquals(0, surgeryPage.getTotalElements());
        assertEquals(0, surgeryPage.getTotalPages());
        assertEquals(1, surgeryPage.getNumber());
        assertFalse(surgeryPage.hasNext());
        assertTrue(surgeryPage.hasPrevious());
    }



}