package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses ={UserMapper.class})
public interface DentistMapper {

    @Mapping(target="id",ignore = true)
    @Mapping(target="appointments",ignore = true)
    @Mapping(target = "user", ignore = true)

    Dentist dentistRequestDtoToDentist(DentistRequestDto dentistRequestDto);


    DentistResponseDto dentistToDentistResponseDto(Dentist dentist);

    List<DentistResponseDto> dentistToDentistResponseDto(List<Dentist> dentists);

    @Mapping(target="id",ignore = true)
    @Mapping(target="user",ignore = true)
    @Mapping(target="appointments",ignore = true)
    void updateDentistFromDentistRequestDto(DentistRequestDto dentistRequestDto, @MappingTarget Dentist dentist);

    DentistBasicResponseDto dentistToDentistBasicResponseDto(Dentist dentist);
}
