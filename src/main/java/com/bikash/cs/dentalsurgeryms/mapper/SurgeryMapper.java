package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.SurgeryRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {AddressMapper.class})
public interface SurgeryMapper {
    @Mapping(source = "name", target = "surgeryName")
    @Mapping(source = "addressRequestDto", target = "address")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(target = "id", ignore = true)
    @Mapping(target="appointments",ignore = true)
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

    @Mapping(source = "surgeryName", target = "name")
    @Mapping(target = "location", expression = "java(surgery.getAddress() != null ? formatAddress(surgery.getAddress()) : null)")
    SurgeryBasicResponseDto surgeryToSurgeryBasicResponseDto(Surgery surgery);

    default String formatAddress(Address address) {
        return address.getStreet() + ", " + address.getCity() + ", " + address.getZipCode();
    }

}
