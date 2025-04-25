package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.DentistRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.DentistResponseDto;
import com.bikash.cs.dentalsurgeryms.service.DentistService;
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
@RequestMapping("/api/v1/dentists")
@RequiredArgsConstructor
public class DentistController {
    private final DentistService dentistService;
    @PostMapping
    public ResponseEntity<DentistResponseDto> createDentist(@Valid @RequestBody DentistRequestDto dentistRequestDto) {
        DentistResponseDto createdSurgery = dentistService.createDentist(dentistRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSurgery);
    }


    @GetMapping("/{id}")
    public ResponseEntity<DentistResponseDto> getDentistById(@PathVariable Long id) {
        DentistResponseDto dentist = dentistService.getDentistById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dentist);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<DentistResponseDto>>> getAllSurgeries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<DentistResponseDto> pagedResourcesAssembler
    ) {
        Page<DentistResponseDto> surgeriesPage = dentistService.getAllDentists(page, pageSize, sortDirection, sortBy);
        PagedModel<EntityModel<DentistResponseDto>> pagedModel = pagedResourcesAssembler.toModel(surgeriesPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DentistResponseDto> updateDentist(@PathVariable Long id, @Valid @RequestBody DentistRequestDto dentistRequestDto) {
        DentistResponseDto  dentistResponseDto = dentistService.updateDentist(id, dentistRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(dentistResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DentistResponseDto> deleteDentist(@PathVariable Long id) {
        dentistService.deleteDentist(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
