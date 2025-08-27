package com.example.bookify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User email address", example = "john.doe@example.com")
    @NotBlank(message = "Email mustn't be blank")
    @Email(message = "Incorrect email format")
    private String email;

    @Schema(description = "User password (min 8, max 64 characters)", example = "securePassword123")
    @NotBlank(message = "Password mustn't be blank")
    @Size(min = 8, max = 64, message = "Password must be between 6 and 64 characters long")
    private String password;
}
