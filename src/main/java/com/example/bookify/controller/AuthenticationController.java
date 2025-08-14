package com.example.bookify.controller;

import com.example.bookify.dto.AuthenticationRequest;
import com.example.bookify.dto.AuthenticationResponse;
import com.example.bookify.dto.RegisterRequest;
import com.example.bookify.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("New registration request for email={}", request.getEmail());
        AuthenticationResponse response = service.register(request);
        log.debug("Registration completed for email={}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        log.info("Authentication attempt for email={}", request.getEmail());
        AuthenticationResponse response = service.authenticate(request);
        log.debug("Authentication successful for email={}", request.getEmail());
        return ResponseEntity.ok(response);
    }
}
