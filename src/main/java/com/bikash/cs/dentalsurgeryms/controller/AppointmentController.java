package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.AppointmentRequestDto;
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
    public ResponseEntity<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> createAppointment(@Valid @RequestBody AppointmentRequestDto appointmentCreateDto) {
        com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto appointmentResponseDto = appointmentService.createAppointment(appointmentCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentResponseDto);
    }



    @GetMapping("/{id}")
    public ResponseEntity<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto appointmentResponseDto = appointmentService.getAppointmentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponseDto);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedModel<EntityModel<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto>>> getAppointmentsByPatientId(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> pagedResourcesAssembler
            ){
        Page<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> appointmentsPage = appointmentService.getAppointmentsByPatientId(patientId, page, pageSize,  sortDirection,sortBy);
        PagedModel<EntityModel<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto>> pagedModel = pagedResourcesAssembler.toModel(appointmentsPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @GetMapping("/dentist/{dentistId}")
    public ResponseEntity<PagedModel<EntityModel<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto>>> getAppointmentsByDentistId(
            @PathVariable Long dentistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> pagedResourcesAssembler
    ){
        Page<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> appointmentsPage = appointmentService.getAppointmentsByDentistId(dentistId, page, pageSize,  sortDirection,sortBy);
        PagedModel<EntityModel<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto>> pagedModel = pagedResourcesAssembler.toModel(appointmentsPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> cancelAppointment(@PathVariable Long id) {
        com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto appointmentResponseDto = appointmentService.cancelAppointment(id);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRequestDto appointmentCreateDto) {
        com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto appointmentResponseDto = appointmentService.updateAppointment(id, appointmentCreateDto);
        return ResponseEntity.status(HttpStatus.OK).body(appointmentResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
