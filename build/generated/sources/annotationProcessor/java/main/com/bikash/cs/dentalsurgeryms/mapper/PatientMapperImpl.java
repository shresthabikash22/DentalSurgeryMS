package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-26T04:08:18-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Patient patientRequestDtoToPatient(PatientRequestDto patientRequestDto) {
        if ( patientRequestDto == null ) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        patient.firstName( patientRequestDto.firstName() );
        patient.lastName( patientRequestDto.lastName() );
        patient.phoneNumber( patientRequestDto.phoneNumber() );
        patient.email( patientRequestDto.email() );
        patient.dateOfBirth( patientRequestDto.dateOfBirth() );
        patient.address( addressMapper.addressRequestDtoToAddress( patientRequestDto.address() ) );

        return patient.build();
    }

    @Override
    public PatientResponseDto patientToPatientResponseDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        Long id = null;
        String firstName = null;
        String lastName = null;
        String phoneNumber = null;
        String email = null;
        boolean hasUnpaidBill = false;
        LocalDate dateOfBirth = null;
        AddressResponseDto address = null;
        List<AppointmentResponseDto> appointments = null;

        id = patient.getId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        phoneNumber = patient.getPhoneNumber();
        email = patient.getEmail();
        hasUnpaidBill = patient.isHasUnpaidBill();
        dateOfBirth = patient.getDateOfBirth();
        address = addressMapper.addressToAddressResponseDto( patient.getAddress() );
        appointments = appointmentListToAppointmentResponseDtoList( patient.getAppointments() );

        PatientResponseDto patientResponseDto = new PatientResponseDto( id, firstName, lastName, phoneNumber, email, hasUnpaidBill, dateOfBirth, address, appointments );

        return patientResponseDto;
    }

    @Override
    public PatientBasicResponseDto patientToPatientBasicResponseDto(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        Long id = null;
        String firstName = null;
        String lastName = null;
        String phoneNumber = null;
        String email = null;

        id = patient.getId();
        firstName = patient.getFirstName();
        lastName = patient.getLastName();
        phoneNumber = patient.getPhoneNumber();
        email = patient.getEmail();

        PatientBasicResponseDto patientBasicResponseDto = new PatientBasicResponseDto( id, firstName, lastName, phoneNumber, email );

        return patientBasicResponseDto;
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
