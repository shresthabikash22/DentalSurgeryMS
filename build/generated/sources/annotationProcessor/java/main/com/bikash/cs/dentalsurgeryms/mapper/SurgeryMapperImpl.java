package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.SurgeryRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-25T03:22:08-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class SurgeryMapperImpl implements SurgeryMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Surgery surgeryRequestDtoToSurgery(SurgeryRequestDto surgeryRequestDto) {
        if ( surgeryRequestDto == null ) {
            return null;
        }

        Surgery surgery = new Surgery();

        surgery.setSurgeryName( surgeryRequestDto.name() );
        surgery.setAddress( addressMapper.addressRequestDtoToAddress( surgeryRequestDto.addressRequestDto() ) );
        surgery.setPhoneNumber( surgeryRequestDto.phoneNumber() );
        surgery.setBranchCode( surgeryRequestDto.branchCode() );

        return surgery;
    }

    @Override
    public SurgeryResponseDto surgeryToSurgeryResponseDto(Surgery surgery) {
        if ( surgery == null ) {
            return null;
        }

        Long surgeryId = null;
        String name = null;
        String phoneNumber = null;
        AddressResponseDto addressResponseDto = null;
        String branchCode = null;

        surgeryId = surgery.getId();
        name = surgery.getSurgeryName();
        phoneNumber = surgery.getPhoneNumber();
        addressResponseDto = addressMapper.addressToAddressResponseDto( surgery.getAddress() );
        branchCode = surgery.getBranchCode();

        SurgeryResponseDto surgeryResponseDto = new SurgeryResponseDto( surgeryId, branchCode, name, phoneNumber, addressResponseDto );

        return surgeryResponseDto;
    }

    @Override
    public List<SurgeryResponseDto> surgeryToSurgeryResponseDto(List<Surgery> surgeries) {
        if ( surgeries == null ) {
            return null;
        }

        List<SurgeryResponseDto> list = new ArrayList<SurgeryResponseDto>( surgeries.size() );
        for ( Surgery surgery : surgeries ) {
            list.add( surgeryToSurgeryResponseDto( surgery ) );
        }

        return list;
    }

    @Override
    public SurgeryBasicResponseDto surgeryToSurgeryBasicResponseDto(Surgery surgery) {
        if ( surgery == null ) {
            return null;
        }

        String name = null;
        Long id = null;
        String branchCode = null;

        name = surgery.getSurgeryName();
        id = surgery.getId();
        branchCode = surgery.getBranchCode();

        String location = surgery.getAddress() != null ? formatAddress(surgery.getAddress()) : null;

        SurgeryBasicResponseDto surgeryBasicResponseDto = new SurgeryBasicResponseDto( id, name, branchCode, location );

        return surgeryBasicResponseDto;
    }
}
