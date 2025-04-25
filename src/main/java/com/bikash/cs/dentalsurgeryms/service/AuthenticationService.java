package com.bikash.cs.dentalsurgeryms.service;

import com.bikash.cs.dentalsurgeryms.dto.request.AuthenticationRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.request.RegisterRequestDto;
import com.bikash.cs.dentalsurgeryms.dto.response.AuthenticationResponse;


public interface AuthenticationService {

    public AuthenticationResponse authenticate(AuthenticationRequestDto authenticationRequestDto);

    public AuthenticationResponse register(RegisterRequestDto registerRequestDto);
}
