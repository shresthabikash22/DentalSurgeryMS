package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.model.Address;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-24T03:41:42-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class DentistMapperImpl implements DentistMapper {

    @Autowired
    private UserMapper userMapper;

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
        dentist.setUser( userMapper.userRequestDtoToUser( dentistRequestDto.user() ) );

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
        dentist.setUser( userMapper.userRequestDtoToUser( dentistRequestDto.user() ) );
    }

    protected AddressResponseDto addressToAddressResponseDto(Address address) {
        if ( address == null ) {
            return null;
        }

        String street = null;
        String city = null;
        String state = null;
        String zipCode = null;

        street = address.getStreet();
        city = address.getCity();
        state = address.getState();
        zipCode = address.getZipCode();

        Long addressId = null;

        AddressResponseDto addressResponseDto = new AddressResponseDto( addressId, street, city, state, zipCode );

        return addressResponseDto;
    }

    protected PatientResponseDto patientToPatientResponseDto(Patient patient) {
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
        address = addressToAddressResponseDto( patient.getAddress() );
        appointments = appointmentListToAppointmentResponseDtoList( patient.getAppointments() );

        PatientResponseDto patientResponseDto = new PatientResponseDto( id, firstName, lastName, phoneNumber, email, hasUnpaidBill, dateOfBirth, address, appointments );

        return patientResponseDto;
    }

    protected SurgeryResponseDto surgeryToSurgeryResponseDto(Surgery surgery) {
        if ( surgery == null ) {
            return null;
        }

        String branchCode = null;
        String phoneNumber = null;

        branchCode = surgery.getBranchCode();
        phoneNumber = surgery.getPhoneNumber();

        Long surgeryId = null;
        String name = null;
        AddressResponseDto addressResponseDto = null;

        SurgeryResponseDto surgeryResponseDto = new SurgeryResponseDto( surgeryId, branchCode, name, phoneNumber, addressResponseDto );

        return surgeryResponseDto;
    }

    protected AppointmentResponseDto appointmentToAppointmentResponseDto(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        Long id = null;
        LocalDateTime appointmentDateTime = null;
        AppointmentStatus status = null;
        PatientResponseDto patient = null;
        DentistResponseDto dentist = null;
        SurgeryResponseDto surgery = null;

        id = appointment.getId();
        appointmentDateTime = appointment.getAppointmentDateTime();
        status = appointment.getStatus();
        patient = patientToPatientResponseDto( appointment.getPatient() );
        dentist = dentistToDentistResponseDto( appointment.getDentist() );
        surgery = surgeryToSurgeryResponseDto( appointment.getSurgery() );

        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto( id, appointmentDateTime, status, patient, dentist, surgery );

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
