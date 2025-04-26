package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.SurgeryRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.dto.response.SurgeryResponseDto;
import com.bikash.cs.dentalsurgeryms.service.SurgeryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/surgeries")
@RequiredArgsConstructor
public class SurgeryController {

    private final SurgeryService surgeryService;

    @PostMapping
    public ResponseEntity<SurgeryResponseDto> createSurgery(@Valid @RequestBody SurgeryRequestDto surgeryRequestDto) {
        SurgeryResponseDto createdSurgery = surgeryService.createSurgery(surgeryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSurgery);
    }


    @GetMapping("/{branchCode}")
    public ResponseEntity<SurgeryResponseDto> getSurgeryByBranchCode(@PathVariable String branchCode) {
        SurgeryResponseDto surgery = surgeryService.getSurgeryByBranchCode(branchCode);
        return ResponseEntity.status(HttpStatus.OK).body(surgery);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<SurgeryResponseDto>>> getAllSurgeries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<SurgeryResponseDto> pagedResourcesAssembler
    ) {
        Page<SurgeryResponseDto> surgeriesPage = surgeryService.getAllSurgeries(page, pageSize, sortDirection, sortBy);
        PagedModel<EntityModel<SurgeryResponseDto>> pagedModel = pagedResourcesAssembler.toModel(surgeriesPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PutMapping("/{branchCode}")
    public ResponseEntity<SurgeryResponseDto> updateSurgery(@PathVariable String branchCode, @Valid @RequestBody SurgeryRequestDto surgeryRequestDto) {
        SurgeryResponseDto  surgeryResponseDto = surgeryService.updateSurgery(branchCode, surgeryRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(surgeryResponseDto);
    }

    @DeleteMapping("/{branchCode}")
    public ResponseEntity<SurgeryResponseDto> deleteSurgery(@PathVariable String branchCode) {
        surgeryService.deleteSurgery(branchCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
