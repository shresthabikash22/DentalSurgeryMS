package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AppointmentResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.PatientResponseDto;
import com.bikash.cs.dentalsurgeryms.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

//    @PostMapping
//    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patientRequestDto) {
//        PatientResponseDto createdPatient = patientService.createPatient(patientRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id) {
        PatientResponseDto patient = patientService.getPatientById(id);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PatientResponseDto>>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<PatientResponseDto> pagedResourcesAssembler
    ){
        Page<PatientResponseDto> patientsPage = patientService.getAllPatients(page, pageSize,sortDirection, sortBy);
        PagedModel<EntityModel<PatientResponseDto>> pagedModel = pagedResourcesAssembler.toModel(patientsPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequestDto surgeryRequestDto) {
        PatientResponseDto  patientResponseDto = patientService.updatePatient(id, surgeryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(patientResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientResponseDto> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/appointments")
        public ResponseEntity<PagedModel<EntityModel<AppointmentResponseDto>>> getAppointmentsByPatientId(
                @PathVariable Long id,
                @AuthenticationPrincipal UserDetails userDetails,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int pageSize,
                @RequestParam(defaultValue = "id") String sortBy,
                @RequestParam(defaultValue = "asc") String sortDirection,
                PagedResourcesAssembler<AppointmentResponseDto> pagedResourcesAssembler
        ) {
            Page<AppointmentResponseDto> appointmentResponseDtos =
                    patientService.getAppointmentsByPatientId(id,userDetails,page,pageSize,sortDirection,sortBy);
            PagedModel<EntityModel<AppointmentResponseDto>>  pagedModel = pagedResourcesAssembler.toModel(appointmentResponseDtos);
            return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
        }
    
    
}
