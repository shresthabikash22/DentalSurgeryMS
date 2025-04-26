package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses={AddressMapper.class, UserMapper.class})
public interface PatientMapper {
    @Mapping(target = "user", ignore = true)
    Patient patientRequestDtoToPatient(PatientRequestDto patientRequestDto);
    PatientResponseDto patientToPatientResponseDto(Patient patient);
    List<PatientResponseDto> patientToPatientResponseDtoPage(List<Patient> patients);
    void updatePatientFromPatientRequestDto(PatientRequestDto patientRequestDto, @MappingTarget Patient patient);
    PatientBasicResponseDto patientToPatientBasicResponseDto(Patient patient);
}
