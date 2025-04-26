package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryBasicResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
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
public class AppointmentMapperImpl implements AppointmentMapper {

    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private DentistMapper dentistMapper;
    @Autowired
    private SurgeryMapper surgeryMapper;

    @Override
    public Appointment appointmentRequestDtoToAppointment(AppointmentRequestDto appointmentResponseDto) {
        if ( appointmentResponseDto == null ) {
            return null;
        }

        Appointment.AppointmentBuilder appointment = Appointment.builder();

        appointment.patient( appointmentRequestDtoToPatient( appointmentResponseDto ) );
        appointment.dentist( appointmentRequestDtoToDentist( appointmentResponseDto ) );
        appointment.surgery( appointmentRequestDtoToSurgery( appointmentResponseDto ) );
        appointment.appointmentDateTime( appointmentResponseDto.appointmentDateTime() );

        appointment.status( com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus.SCHEDULED );

        return appointment.build();
    }

    @Override
    public AppointmentResponseDto appointmentToAppointmentResponseDto(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentStatus status = null;
        PatientBasicResponseDto patientBasicResponseDto = null;
        DentistBasicResponseDto dentistBasicResponseDto = null;
        SurgeryBasicResponseDto surgeryBasicResponseDto = null;
        Long id = null;
        LocalDateTime appointmentDateTime = null;

        status = appointment.getStatus();
        patientBasicResponseDto = patientMapper.patientToPatientBasicResponseDto( appointment.getPatient() );
        dentistBasicResponseDto = dentistMapper.dentistToDentistBasicResponseDto( appointment.getDentist() );
        surgeryBasicResponseDto = surgeryMapper.surgeryToSurgeryBasicResponseDto( appointment.getSurgery() );
        id = appointment.getId();
        appointmentDateTime = appointment.getAppointmentDateTime();

        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto( id, appointmentDateTime, status, patientBasicResponseDto, dentistBasicResponseDto, surgeryBasicResponseDto );

        return appointmentResponseDto;
    }

    @Override
    public List<AppointmentResponseDto> appointmentsToAppointmentResponseDtos(List<Appointment> appointments) {
        if ( appointments == null ) {
            return null;
        }

        List<AppointmentResponseDto> list = new ArrayList<AppointmentResponseDto>( appointments.size() );
        for ( Appointment appointment : appointments ) {
            list.add( appointmentToAppointmentResponseDto( appointment ) );
        }

        return list;
    }

    protected Patient appointmentRequestDtoToPatient(AppointmentRequestDto appointmentRequestDto) {
        if ( appointmentRequestDto == null ) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        patient.id( appointmentRequestDto.patientId() );

        return patient.build();
    }

    protected Dentist appointmentRequestDtoToDentist(AppointmentRequestDto appointmentRequestDto) {
        if ( appointmentRequestDto == null ) {
            return null;
        }

        Dentist.DentistBuilder dentist = Dentist.builder();

        dentist.id( appointmentRequestDto.dentistId() );

        return dentist.build();
    }

    protected Surgery appointmentRequestDtoToSurgery(AppointmentRequestDto appointmentRequestDto) {
        if ( appointmentRequestDto == null ) {
            return null;
        }

        Surgery.SurgeryBuilder surgery = Surgery.builder();

        surgery.id( appointmentRequestDto.surgeryId() );

        return surgery.build();
    }
}
