package com.example.bookify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;

    @NotBlank(message = "Email mustn't be blank")
    @Email(message = "Incorrect email format")
    private String email;

    @NotBlank(message = "Password mustn't be blank")
    @Size(min = 8, max = 64, message = "Password must be between 6 and 64 characters long")
    private String password;
}
