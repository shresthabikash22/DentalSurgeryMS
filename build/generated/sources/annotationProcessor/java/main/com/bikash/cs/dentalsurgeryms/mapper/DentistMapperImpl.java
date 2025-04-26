package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-25T16:17:06-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class DentistMapperImpl implements DentistMapper {

    @Override
    public Dentist dentistRequestDtoToDentist(DentistRequestDto dentistRequestDto) {
        if ( dentistRequestDto == null ) {
            return null;
        }

        Dentist dentist = new Dentist();

        dentist.setFirstName( dentistRequestDto.firstName() );
        dentist.setLastName( dentistRequestDto.lastName() );
        dentist.setPhoneNumber( dentistRequestDto.phoneNumber() );
        dentist.setEmail( dentistRequestDto.email() );
        dentist.setSpecialization( dentistRequestDto.specialization() );

        return dentist;
    }

    @Override
    public DentistResponseDto dentistToDentistResponseDto(Dentist dentist) {
        if ( dentist == null ) {
            return null;
        }

        Long id = null;
        String firstName = null;
        String lastName = null;
        String phoneNumber = null;
        String email = null;
        String specialization = null;
        List<AppointmentResponseDto> appointments = null;

        id = dentist.getId();
        firstName = dentist.getFirstName();
        lastName = dentist.getLastName();
        phoneNumber = dentist.getPhoneNumber();
        email = dentist.getEmail();
        specialization = dentist.getSpecialization();
        appointments = appointmentListToAppointmentResponseDtoList( dentist.getAppointments() );

        DentistResponseDto dentistResponseDto = new DentistResponseDto( id, firstName, lastName, phoneNumber, email, specialization, appointments );

        return dentistResponseDto;
    }

    @Override
    public List<DentistResponseDto> dentistToDentistResponseDto(List<Dentist> dentists) {
        if ( dentists == null ) {
            return null;
        }

        List<DentistResponseDto> list = new ArrayList<DentistResponseDto>( dentists.size() );
        for ( Dentist dentist : dentists ) {
            list.add( dentistToDentistResponseDto( dentist ) );
        }

        return list;
    }

    @Override
    public void updateDentistFromDentistRequestDto(DentistRequestDto dentistRequestDto, Dentist dentist) {
        if ( dentistRequestDto == null ) {
            return;
        }

        dentist.setFirstName( dentistRequestDto.firstName() );
        dentist.setLastName( dentistRequestDto.lastName() );
        dentist.setPhoneNumber( dentistRequestDto.phoneNumber() );
        dentist.setEmail( dentistRequestDto.email() );
        dentist.setSpecialization( dentistRequestDto.specialization() );
    }

    @Override
    public DentistBasicResponseDto dentistToDentistBasicResponseDto(Dentist dentist) {
        if ( dentist == null ) {
            return null;
        }

        Long id = null;
        String firstName = null;
        String lastName = null;
        String specialization = null;

        id = dentist.getId();
        firstName = dentist.getFirstName();
        lastName = dentist.getLastName();
        specialization = dentist.getSpecialization();

        DentistBasicResponseDto dentistBasicResponseDto = new DentistBasicResponseDto( id, firstName, lastName, specialization );

        return dentistBasicResponseDto;
    }

    protected AppointmentResponseDto appointmentToAppointmentResponseDto(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        Long id = null;
        LocalDateTime appointmentDateTime = null;
        AppointmentStatus status = null;

        id = appointment.getId();
        appointmentDateTime = appointment.getAppointmentDateTime();
        status = appointment.getStatus();

        PatientBasicResponseDto patientBasicResponseDto = null;
        DentistBasicResponseDto dentistBasicResponseDto = null;
        SurgeryBasicResponseDto surgeryBasicResponseDto = null;

        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto( id, appointmentDateTime, status, patientBasicResponseDto, dentistBasicResponseDto, surgeryBasicResponseDto );

        return appointmentResponseDto;
    }

    protected List<AppointmentResponseDto> appointmentListToAppointmentResponseDtoList(List<Appointment> list) {
        if ( list == null ) {
            return null;
        }

        List<AppointmentResponseDto> list1 = new ArrayList<AppointmentResponseDto>( list.size() );
        for ( Appointment appointment : list ) {
            list1.add( appointmentToAppointmentResponseDto( appointment ) );
        }

        return list1;
    }
}
