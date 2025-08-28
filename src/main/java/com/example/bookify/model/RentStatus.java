package com.example.bookify.model;

import io.swagger.v3.oas.annotations.media.Schema;

public enum RentStatus {
    @Schema(description="Enum that describes active rents")
    ACTIVE,
    @Schema(description="Enum that describes returned rents")
    RETURNED,
    @Schema(description="Enum that describes overdue rents")
    OVERDUE
}
