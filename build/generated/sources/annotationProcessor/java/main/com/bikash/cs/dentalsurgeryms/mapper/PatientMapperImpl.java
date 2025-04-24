package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
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
    date = "2025-04-24T03:32:32-0500",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Patient patientRequestDtoToPatient(PatientRequestDto patientRequestDto) {
        if ( patientRequestDto == null ) {
            return null;
        }

        Patient patient = new Patient();

        patient.setFirstName( patientRequestDto.firstName() );
        patient.setLastName( patientRequestDto.lastName() );
        patient.setPhoneNumber( patientRequestDto.phoneNumber() );
        patient.setEmail( patientRequestDto.email() );
        patient.setDateOfBirth( patientRequestDto.dateOfBirth() );
        patient.setUser( userMapper.userRequestDtoToUser( patientRequestDto.user() ) );
        patient.setAddress( addressMapper.addressRequestDtoToAddress( patientRequestDto.address() ) );

        return patient;
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
    public List<PatientResponseDto> patientToPatientResponseDtoPage(List<Patient> patients) {
        if ( patients == null ) {
            return null;
        }

        List<PatientResponseDto> list = new ArrayList<PatientResponseDto>( patients.size() );
        for ( Patient patient : patients ) {
            list.add( patientToPatientResponseDto( patient ) );
        }

        return list;
    }

    @Override
    public void updatePatientFromPatientRequestDto(PatientRequestDto patientRequestDto, Patient patient) {
        if ( patientRequestDto == null ) {
            return;
        }

        patient.setFirstName( patientRequestDto.firstName() );
        patient.setLastName( patientRequestDto.lastName() );
        patient.setPhoneNumber( patientRequestDto.phoneNumber() );
        patient.setEmail( patientRequestDto.email() );
        patient.setDateOfBirth( patientRequestDto.dateOfBirth() );
        patient.setUser( userMapper.userRequestDtoToUser( patientRequestDto.user() ) );
        patient.setAddress( addressMapper.addressRequestDtoToAddress( patientRequestDto.address() ) );
    }

    protected DentistResponseDto dentistToDentistResponseDto(Dentist dentist) {
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
