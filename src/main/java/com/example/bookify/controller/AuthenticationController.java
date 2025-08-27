package com.example.bookify.controller;

import com.example.bookify.dto.AuthenticationRequest;
import com.example.bookify.dto.AuthenticationResponse;
import com.example.bookify.dto.RegisterRequest;
import com.example.bookify.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthenticationController {

    private final AuthenticationService service;


    @Operation(
            summary = "Register a new user",
            description = "Creates a new account for the user with provided first name, last name, email and password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully registered",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request data",
                            content = @Content(schema = @Schema(example = "{ \"error\": \"Email mustn't be blank\" }")))
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("New registration request for email={}", request.getEmail());
        AuthenticationResponse response = service.register(request);
        log.debug("Registration completed for email={}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates user with provided email and password. Returns a JWT token if credentials are valid.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authentication successful",
                            content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials",
                            content = @Content(schema = @Schema(example = "{ \"error\": \"Invalid email or password\" }")))
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        log.info("Authentication attempt for email={}", request.getEmail());
        AuthenticationResponse response = service.authenticate(request);
        log.debug("Authentication successful for email={}", request.getEmail());
        return ResponseEntity.ok(response);
    }
}
