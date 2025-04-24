package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.PatientRequestDto;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patientRequestDto) {
        PatientResponseDto createdPatient = patientService.createPatient(patientRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @GetMapping("/{email}")
    public ResponseEntity<PatientResponseDto> getPatientByEmail(@PathVariable String email) {
        PatientResponseDto patient = patientService.getPatientByEmail(email);
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

    @PutMapping("/{email}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable String email, @Valid @RequestBody PatientRequestDto surgeryRequestDto) {
        PatientResponseDto  patientResponseDto = patientService.updatePatient(email, surgeryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(patientResponseDto);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<PatientResponseDto> deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    
}
