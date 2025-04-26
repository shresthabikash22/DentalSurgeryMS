package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    Address addressRequestDtoToAddress(AddressRequestDto addressRequestDto);

    @Mapping(source = "id", target = "addressId")
    AddressResponseDto addressToAddressResponseDto(Address address);
//    @Mapping(source = "id", target = "addressId")
    List<AddressResponseDto> addressToAddressResponseDto(List<Address> addresses);
}
