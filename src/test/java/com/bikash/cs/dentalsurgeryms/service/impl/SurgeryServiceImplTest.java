package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.request.SurgeryRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.exception.ADSIllegalStateException;
import com.bikash.cs.dentalsurgeryms.exception.DuplicateResourceException;
import com.bikash.cs.dentalsurgeryms.exception.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.SurgeryMapper;
import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import com.bikash.cs.dentalsurgeryms.repository.AddressRepository;
import com.bikash.cs.dentalsurgeryms.repository.SurgeryRepository;
import com.bikash.cs.dentalsurgeryms.service.AppointmentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurgeryServiceImplTest {
    @Mock
    private SurgeryRepository surgeryRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private SurgeryMapper surgeryMapper;
    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private SurgeryServiceImpl surgeryService;

    private Address address;
    private AddressRequestDto addressRequestDto;
    private AddressResponseDto addressResponseDto;
    private Surgery surgery;
    private SurgeryRequestDto surgeryRequestDto;
    private SurgeryResponseDto surgeryResponseDto;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id(10L)
                .street("214 East Road")
                .city("CityA")
                .state("TA")
                .zipCode("87323")
                .build();

        addressRequestDto = new AddressRequestDto("214 East Road", "CityA", "TA", "87323");
        addressResponseDto = new AddressResponseDto(10L, "214 East Road", "CityA", "TA", "87323");

        surgery = Surgery.builder()
                .id(1L)
                .phoneNumber("+87344234234234")
                .branchCode("BR-001")
                .surgeryName("The Smile Dental")
                .address(address)
                .build();

        surgeryRequestDto = new SurgeryRequestDto("SC-TR3","The Smile Dental","+87344234234234",addressRequestDto);


        surgeryResponseDto = new SurgeryResponseDto(1L,"SC-TR3","The Smile Dental","+87344234234234",addressResponseDto);

    }

    @Test
    @DisplayName("Create surgery when branch code and address are unique")
    void givenSurgeryRequestDto_whenCreate_thenReturnSavedSurgeryResponseDto() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.empty());
        when(addressRepository.findByStreet(addressRequestDto.street())).thenReturn(Optional.empty());
        when(surgeryMapper.surgeryRequestDtoToSurgery(surgeryRequestDto)).thenReturn(surgery);
        when(surgeryRepository.save(Mockito.any(Surgery.class))).thenReturn(surgery);
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        SurgeryResponseDto savedSurgery = surgeryService.createSurgery(surgeryRequestDto);

        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
        verify(addressRepository, times(1)).findByStreet(addressRequestDto.street());
        verify(surgeryRepository, times(1)).save(surgery);
        Assertions.assertThat(savedSurgery).isEqualTo(surgeryResponseDto);
    }

    @Test
    @DisplayName("Create surgery with existing branch code throws DuplicateResourceException")
    void givenDuplicateBranchCode_whenCreate_thenThrowDuplicateResourceException() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.of(surgery));
        assertThrows(DuplicateResourceException.class, () -> surgeryService.createSurgery(surgeryRequestDto));
        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
        verify(addressRepository, never()).findByStreet(anyString());
    }

    @Test
    @DisplayName("Create surgery with existing address throws DuplicateResourceException")
    void givenDuplicateAddress_whenCreate_thenThrowDuplicateResourceException() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.empty());
        when(addressRepository.findByStreet(addressRequestDto.street())).thenReturn(Optional.of(address));
        assertThrows(DuplicateResourceException.class, () -> surgeryService.createSurgery(surgeryRequestDto));
        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
        verify(addressRepository, times(1)).findByStreet(addressRequestDto.street());
    }

    @Test
    @DisplayName("Get surgery by branch code when exists")
    void givenExistingSurgery_whenGetByBranchCode_thenReturnSurgeryResponseDto() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.of(surgery));
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        SurgeryResponseDto response = surgeryService.getSurgeryByBranchCode(surgeryRequestDto.branchCode());

        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
        Assertions.assertThat(response).isEqualTo(surgeryResponseDto);
    }

    @Test
    @DisplayName("Get surgery by branch code when does not exist throws exception")
    void givenNonExistingSurgery_whenGetByBranchCode_thenThrowException() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> surgeryService.getSurgeryByBranchCode(surgeryRequestDto.branchCode()));
        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
    }

    @Test
    @DisplayName("Get all surgeries returns page of SurgeryResponseDto")
    void givenSurgeries_whenGetAllSurgeries_thenReturnPageOfSurgeryResponseDtos() {
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "branchCode");
        Page<Surgery> surgeryPage = new PageImpl<>(List.of(surgery));
        when(surgeryRepository.findAll(pageable)).thenReturn(surgeryPage);
        when(surgeryMapper.surgeryToSurgeryResponseDto(surgery)).thenReturn(surgeryResponseDto);

        Page<SurgeryResponseDto> result = surgeryService.getAllSurgeries(0, 2, "ASC", "branchCode");
        Assertions.assertThat(result.getContent()).containsExactly(surgeryResponseDto);
    }

    @Test
    @DisplayName("Update surgery when branch code exists should return updated surgeryResponseDto")
    void givenExistingSurgery_whenUpdateSurgery_thenReturnUpdatedSurgeryResponseDto() {
        SurgeryRequestDto updatedRequestDto = new SurgeryRequestDto("SC-TR3","The Smile Dental","+87344234234234",addressRequestDto);

        Surgery updatedSurgery = new Surgery("The Smile Dental","SC-TR3","+87344234234234",address);

        when(surgeryRepository.findByBranchCode("BR-001")).thenReturn(Optional.of(surgery));
        when(surgeryMapper.surgeryRequestDtoToSurgery(updatedRequestDto)).thenReturn(updatedSurgery);
        when(surgeryRepository.save(any(Surgery.class))).thenReturn(updatedSurgery);
        when(surgeryMapper.surgeryToSurgeryResponseDto(updatedSurgery)).thenReturn(surgeryResponseDto);

        SurgeryResponseDto responseDto = surgeryService.updateSurgery("BR-001", updatedRequestDto);

        verify(surgeryRepository, times(1)).findByBranchCode("BR-001");
        verify(surgeryRepository, times(1)).save(updatedSurgery);
        Assertions.assertThat(responseDto).isEqualTo(surgeryResponseDto);
    }

    @Test
    @DisplayName("Update surgery with another branch code that exists throws duplicate exception")
    void givenAnotherSurgeryWithSameBranchCode_whenUpdate_thenThrowDuplicateResourceException() {
        SurgeryRequestDto updatedRequestDto = new SurgeryRequestDto("SC-TR3","The Smile Dental","+87344234234234",addressRequestDto);
        Surgery anotherSurgery = new Surgery("The Smile Dental","SC-TR3","+87344234234234",address);

        when(surgeryRepository.findByBranchCode("BR-001")).thenReturn(Optional.of(surgery));
        when(surgeryMapper.surgeryRequestDtoToSurgery(updatedRequestDto)).thenReturn(anotherSurgery);
        when(surgeryRepository.findByBranchCode("SC-TR3")).thenReturn(Optional.of(anotherSurgery));

        assertThrows(DuplicateResourceException.class, () -> surgeryService.updateSurgery("BR-001", updatedRequestDto));
        verify(surgeryRepository, times(1)).findByBranchCode("BR-001");
        verify(surgeryRepository, times(1)).findByBranchCode("SC-TR3");
    }

    @Test
    @DisplayName("Update surgery with another address that exists throws duplicate exception")
    void givenAnotherSurgeryWithSameAddress_whenUpdate_thenThrowDuplicateResourceException() {
        AddressRequestDto newAddressRequestDto = new AddressRequestDto("214 East Road", "CityA", "TA", "87323");
        Address anotherAddress = Address.builder().id(20L).street("2nd Ave").city("CityA").build();
        SurgeryRequestDto updatedRequestDto =  new SurgeryRequestDto("SC-TR3","The Smile Dental","+87344234234234",addressRequestDto);
        Surgery updatedSurgery = Surgery.builder().branchCode("SC-TR3").surgeryName("The Smile Dental").phoneNumber("+87344234234234").address(anotherAddress).build();

        when(surgeryRepository.findByBranchCode("BR001")).thenReturn(Optional.of(surgery));
        when(surgeryMapper.surgeryRequestDtoToSurgery(updatedRequestDto)).thenReturn(updatedSurgery);
        when(addressRepository.findByStreet("2nd Ave")).thenReturn(Optional.of(anotherAddress));

        assertThrows(DuplicateResourceException.class, () -> surgeryService.updateSurgery("BR001", updatedRequestDto));
        verify(surgeryRepository, times(1)).findByBranchCode("BR001");
        verify(addressRepository, times(1)).findByStreet("2nd Ave");
    }

    @Test
    @DisplayName("Update surgery that does not exist throws ResourceNotFoundException")
    void givenNonExistingSurgery_whenUpdateSurgery_thenThrowException() {
        when(surgeryRepository.findByBranchCode("BR001")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> surgeryService.updateSurgery("BR001", surgeryRequestDto));
        verify(surgeryRepository, times(1)).findByBranchCode("BR001");
    }

    @Test
    @DisplayName("Delete surgery when exists and no appointments")
    void givenExistingSurgeryWithoutAppointments_whenDeleteSurgery_thenDelete() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.of(surgery));
        when(appointmentService.hasAppointmentsForSurgery(surgery.getId())).thenReturn(false);

        surgeryService.deleteSurgery(surgeryRequestDto.branchCode());

        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
        verify(appointmentService, times(1)).hasAppointmentsForSurgery(surgery.getId());
        verify(surgeryRepository, times(1)).delete(surgery);
    }

    @Test
    @DisplayName("Delete surgery when has appointments throws ADSIllegalStateException")
    void givenSurgeryWithAppointments_whenDeleteSurgery_thenThrowADSIllegalStateException() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.of(surgery));
        when(appointmentService.hasAppointmentsForSurgery(surgery.getId())).thenReturn(true);

        assertThrows(ADSIllegalStateException.class, () -> surgeryService.deleteSurgery(surgeryRequestDto.branchCode()));

        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
        verify(appointmentService, times(1)).hasAppointmentsForSurgery(surgery.getId());
        verify(surgeryRepository, never()).delete(any(Surgery.class));
    }

    @Test
    @DisplayName("Delete surgery when it does not exist throws ResourceNotFoundException")
    void givenNonExistingSurgery_whenDeleteSurgery_thenThrowException() {
        when(surgeryRepository.findByBranchCode(surgeryRequestDto.branchCode())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> surgeryService.deleteSurgery(surgeryRequestDto.branchCode()));
        verify(surgeryRepository, times(1)).findByBranchCode(surgeryRequestDto.branchCode());
        verify(appointmentService, never()).hasAppointmentsForSurgery(anyLong());
        verify(surgeryRepository, never()).delete(any(Surgery.class));
    }
}