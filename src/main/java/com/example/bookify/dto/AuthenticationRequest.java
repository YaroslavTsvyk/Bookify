package com.example.bookify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Email mustn't be blank")
    @Email(message = "Incorrect email format")
    private String email;

    @NotBlank(message = "Password mustn't be blank")
    private String password;
}
