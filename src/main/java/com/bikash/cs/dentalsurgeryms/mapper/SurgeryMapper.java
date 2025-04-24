package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.SurgeryRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AddressMapper.class})
public interface SurgeryMapper {
    @Mapping(source = "name", target = "surgeryName")
    @Mapping(source = "addressRequestDto", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    Surgery surgeryRequestDtoToSurgery(SurgeryRequestDto surgeryRequestDto);

    @Mapping(source = "id", target = "surgeryId")
    @Mapping(source = "surgeryName", target = "name")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "addressResponseDto")
    SurgeryResponseDto surgeryToSurgeryResponseDto(Surgery surgery);

    @Mapping(source = "surgeryName", target = "name")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "addressResponseDto")
    List<SurgeryResponseDto> surgeryToSurgeryResponseDto(List<Surgery> surgeries);
}
