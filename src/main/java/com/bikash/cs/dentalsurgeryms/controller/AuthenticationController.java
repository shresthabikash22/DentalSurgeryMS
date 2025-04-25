package com.bikash.cs.dentalsurgeryms.controller;

import com.bikash.cs.dentalsurgeryms.dto.request.AuthenticationRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.request.RegisterRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AuthenticationResponse;
import com.bikash.cs.dentalsurgeryms.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody RegisterRequestDto registerRequestDto
    ) {
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(
            @RequestBody AuthenticationRequestDto authenticationRequestDto
    ){
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }
}
