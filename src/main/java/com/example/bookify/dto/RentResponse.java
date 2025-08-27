package com.example.bookify.dto;

import com.example.bookify.model.RentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentResponse {
    @Schema(description = "Unique identifier of the rent", example = "10")
    private Long id;

    @Schema(description = "Title of the rented book", example = "Clean Code")
    private String bookTitle;

    @Schema(description = "Full name of the user who rented the book", example = "Alice Johnson")
    private String userName;

    @Schema(description = "Date when the book was rented", example = "2025-08-25")
    private LocalDate rentDate;

    @Schema(description = "Date when the book was returned (null if not yet returned)", example = "2025-09-01")
    private LocalDate returnDate;

    @Schema(description = "Current status of the rent", example = "ACTIVE")
    private RentStatus status;
}
