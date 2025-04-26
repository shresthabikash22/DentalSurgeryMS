package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.AddressRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AddressResponseDto;
import com.bikash.cs.dentalsurgeryms.service.AddressService;
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
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
     private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDto> createAddress(@Valid @RequestBody AddressRequestDto addressRequestDto) {
        AddressResponseDto createdAddress = addressService.createAddress(addressRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @GetMapping("/{street}")
    public ResponseEntity<AddressResponseDto> getAddressByStreet(@PathVariable String street) {
        AddressResponseDto address = addressService.getAddressByStreet(street);
        return ResponseEntity.status(HttpStatus.OK).body(address);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<AddressResponseDto>>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "street") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            PagedResourcesAssembler<AddressResponseDto> pagedResourcesAssembler
    ) {
        Page<AddressResponseDto> addressesPage = addressService.getAllAddresses(page, pageSize,sortDirection, sortBy);
        PagedModel<EntityModel<AddressResponseDto>> pagedModel = pagedResourcesAssembler.toModel(addressesPage);
        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    @PutMapping("/{street}")
    public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable String street, @Valid @RequestBody AddressRequestDto addressRequestDto) {
        AddressResponseDto updatedAddress = addressService.updateAddress(street, addressRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAddress);
    }

    @DeleteMapping("/{street}")
    public ResponseEntity<AddressResponseDto> deleteAddress(@PathVariable String street) {
        addressService.deleteAddress(street);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
