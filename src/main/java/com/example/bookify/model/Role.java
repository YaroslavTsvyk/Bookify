package com.example.bookify.model;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Role {
    @Schema(description="Enum that describes user role")
    USER,
    @Schema(description="Enum that describes admin role")
    ADMIN
}
