package com.example.bookify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Schema(description = "User email address", example = "user@example.com")
    @NotBlank(message = "Email mustn't be blank")
    @Email(message = "Incorrect email format")
    private String email;

    @Schema(description = "User password", example = "12345678")
    @NotBlank(message = "Password mustn't be blank")
    private String password;
}
