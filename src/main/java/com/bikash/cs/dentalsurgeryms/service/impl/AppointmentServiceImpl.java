package com.bikash.cs.dentalsurgeryms.service.impl;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.enums.AppointmentStatus;
import com.bikash.cs.dentalsurgeryms.exception.general.InvalidOperationException;
import com.bikash.cs.dentalsurgeryms.exception.general.ResourceNotFoundException;
import com.bikash.cs.dentalsurgeryms.mapper.AppointmentMapper;
import com.bikash.cs.dentalsurgeryms.model.Appointment;
import com.bikash.cs.dentalsurgeryms.model.Dentist;
import com.bikash.cs.dentalsurgeryms.model.Patient;
import com.bikash.cs.dentalsurgeryms.model.Surgery;
import com.bikash.cs.dentalsurgeryms.repository.AppointmentRepository;
import com.bikash.cs.dentalsurgeryms.repository.DentistRepository;
import com.bikash.cs.dentalsurgeryms.repository.PatientRepository;
import com.bikash.cs.dentalsurgeryms.repository.SurgeryRepository;
import com.bikash.cs.dentalsurgeryms.service.AppointmentService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final SurgeryRepository surgeryRepository;
    private final AppointmentMapper appointmentMapper;
//    private final EmailService emailService;

    @Override
    public AppointmentResponseDto createAppointment(@Valid AppointmentRequestDto appointmentRequestDto) {
        Patient patient = patientRepository.findById(appointmentRequestDto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient with ID " + appointmentRequestDto.patientId() + " not found"));
        if (patient.isHasUnpaidBill()) {
            throw new InvalidOperationException("Patient has unpaid bill. Cannot create appointment");
        }
        Dentist dentist = dentistRepository.findById(appointmentRequestDto.dentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist with ID " + appointmentRequestDto.dentistId() + " not found"));

        LocalDateTime startOfWeek = appointmentRequestDto.appointmentDateTime().truncatedTo(ChronoUnit.DAYS)
                .with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);
        long appointmentCount = appointmentRepository.findByDentistIdAndAppointmentDateTimeBetweenAndStatus(
                dentist.getId(), startOfWeek, endOfWeek, AppointmentStatus.SCHEDULED).size();
        if (appointmentCount >= 5) {
            throw new InvalidOperationException("Dentist cannot have more then 5 appointments in a week");
        }
        Surgery surgery = surgeryRepository.findById(appointmentRequestDto.surgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Surgery with ID " + appointmentRequestDto.surgeryId() + " not found"));

        Appointment appointment = appointmentMapper.appointmentRequestDtoToAppointment(appointmentRequestDto);
        appointment.setDentist(dentist);
        appointment.setPatient(patient);
        appointment.setSurgery(surgery);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setAppointmentDateTime(LocalDateTime.now());

        patient.getAppointments().add(appointment);
        dentist.getAppointments().add(appointment);

        patientRepository.save(patient);
        dentistRepository.save(dentist);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentResponseDto(savedAppointment);

    }

    @Override
    public AppointmentResponseDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));
        return appointmentMapper.appointmentToAppointmentResponseDto(appointment);
    }

    @Override
    public Page<AppointmentResponseDto> getAppointmentsByPatientId(Long patientId, int page, int pageSize, String sortDirection, String sortBy) {
        if (!patientRepository.findById(patientId).isPresent()) {
            throw new ResourceNotFoundException("Patient with ID " + patientId + " not found");
        }
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortBy
        );
        Page<Appointment> appointments = appointmentRepository.findByPatientId(patientId, pageable);
        return appointments.map(appointmentMapper::appointmentToAppointmentResponseDto);
    }

    @Override
    public Page<AppointmentResponseDto> getAppointmentsByDentistId(Long dentistId, int page, int pageSize, String sortDirection, String sortBy) {
        if (!dentistRepository.findById(dentistId).isPresent()) {
            throw new ResourceNotFoundException("Dentist with ID " + dentistId + " not found");
        }
        Pageable pageable = PageRequest.of(
                page,
                pageSize,
                Sort.Direction.fromString(sortDirection),
                sortBy
        );
        Page<Appointment> appointments = appointmentRepository.findByDentistId(dentistId, pageable);
        return appointments.map(appointmentMapper::appointmentToAppointmentResponseDto);
    }

    @Override
    public AppointmentResponseDto cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidOperationException("Appointment is already cancelled");
        }
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentResponseDto(savedAppointment);
    }

    @Override
    @Transactional
    public AppointmentResponseDto updateAppointment(Long id, @Valid AppointmentRequestDto appointmentRequestDto) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));
        Patient patient = patientRepository.findById(appointmentRequestDto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient with ID " + appointmentRequestDto.patientId() + " not found"));
        if (patient.isHasUnpaidBill()) {
            throw new InvalidOperationException("Patient has unpaid bill. Cannot update appointment");
        }
        Dentist dentist = dentistRepository.findById(appointmentRequestDto.dentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist with ID " + appointmentRequestDto.dentistId() + " not found"));
        Surgery surgery = surgeryRepository.findById(appointmentRequestDto.surgeryId())
                .orElseThrow(() -> new ResourceNotFoundException("Survey with ID " + appointmentRequestDto.surgeryId() + " not found"));
        LocalDateTime startOfWeek = appointmentRequestDto.appointmentDateTime().truncatedTo(ChronoUnit.DAYS)
                .with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);
        long appointmentCount = appointmentRepository.findByDentistIdAndAppointmentDateTimeBetweenAndStatus(
                        dentist.getId(), startOfWeek, endOfWeek, AppointmentStatus.SCHEDULED).stream()
                .filter(app -> !app.getId().equals(id)).count();
        if (appointmentCount >= 5) {
            throw new InvalidOperationException("Dentist cannot have more than 5 appointments in a week");
        }

        existingAppointment.setAppointmentDateTime(appointmentRequestDto.appointmentDateTime());
        existingAppointment.setPatient(patient);
        existingAppointment.setDentist(dentist);
        existingAppointment.setSurgery(surgery);
        existingAppointment.setStatus(appointmentRequestDto.status());
        Appointment savedAppointment = appointmentRepository.save(existingAppointment);
//        try {
//            emailService.sendAppointmentNotification(savedAppointment);
//        } catch (MessagingException e) {
//            throw new AppointmentNotificationFailedException("Failed to send appointment update notification");
//        }

        return appointmentMapper.appointmentToAppointmentResponseDto(savedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with ID " + id + " not found"));
        appointmentRepository.delete(appointment);
    }

    @Override
    public boolean hasAppointmentsForSurgery(Long surgeryId) {
        return appointmentRepository.existsBySurgery_Id(surgeryId);
    }

    @Override
    public boolean hasAppointmentsForDentistAndStatusNot(Dentist dentist, AppointmentStatus status) {
        return appointmentRepository.existsByDentistAndStatusNot(dentist, status);
    }

    @Override
    public boolean hasAppointmentsForPatientAndStatusNot(Patient patient, AppointmentStatus status) {
        return appointmentRepository.existsByPatientAndStatusNot(patient, status);
    }

}
