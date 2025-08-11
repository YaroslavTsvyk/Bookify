package com.example.bookify.service;

import com.example.bookify.dto.AuthenticationRequest;
import com.example.bookify.dto.AuthenticationResponse;
import com.example.bookify.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
