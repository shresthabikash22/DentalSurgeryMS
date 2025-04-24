package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody  AppointmentRequestDto appointmentRequestDto) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.createAppointment(appointmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.getAppointmentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponseDto);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedModel<EntityModel<AppointmentResponseDto>>> getAppointmentsByPatientId(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<AppointmentResponseDto> pagedResourcesAssembler
            ){
        Page<AppointmentResponseDto> appointmentsPage = appointmentService.getAppointmentsByPatientId(patientId, page, pageSize,  sortDirection,sortBy);
        PagedModel<EntityModel<AppointmentResponseDto>> pagedModel = pagedResourcesAssembler.toModel(appointmentsPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @GetMapping("/dentist/{dentistId}")
    public ResponseEntity<PagedModel<EntityModel<AppointmentResponseDto>>> getAppointmentsByDentistId(
            @PathVariable Long dentistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<AppointmentResponseDto> pagedResourcesAssembler
    ){
        Page<AppointmentResponseDto> appointmentsPage = appointmentService.getAppointmentsByDentistId(dentistId, page, pageSize,  sortDirection,sortBy);
        PagedModel<EntityModel<AppointmentResponseDto>> pagedModel = pagedResourcesAssembler.toModel(appointmentsPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(@PathVariable Long id) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.cancelAppointment(id);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRequestDto appointmentRequestDto) {
        AppointmentResponseDto appointmentResponseDto = appointmentService.updateAppointment(id, appointmentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
