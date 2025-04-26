package com.bikash.cs.dentalsurgeryms.mapper;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
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
    Appointment appointmentRequestDtoToAppointment(AppointmentRequestDto appointmentResponseDto);

    @Mapping(source = "status",target = "status")
    @Mapping(source = "patient", target = "patientBasicResponseDto")
    @Mapping(source = "dentist", target = "dentistBasicResponseDto")
    @Mapping(source = "surgery", target = "surgeryBasicResponseDto")
    com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto appointmentToAppointmentResponseDto(Appointment appointment);

    List<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> appointmentsToAppointmentResponseDtos(List<Appointment> appointments);
}

