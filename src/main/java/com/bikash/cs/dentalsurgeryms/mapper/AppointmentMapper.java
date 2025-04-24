package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PatientMapper.class, DentistMapper.class,SurgeryMapper.class})
public interface AppointmentMapper {
    @Mapping(target = "status", expression = "java(com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus.SCHEDULED)")
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "dentist.id", source = "dentistId")
    @Mapping(target = "surgery.id", source = "surgeryId")
    Appointment appointmentRequestDtoToAppointment(AppointmentRequestDto appointmentRequestDto);

    AppointmentResponseDto appointmentToAppointmentResponseDto(Appointment appointment);

    List<AppointmentResponseDto> appointmentsToAppointmentResponseDtos(List<Appointment> appointments);
}

