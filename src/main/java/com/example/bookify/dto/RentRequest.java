package com.example.bookify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentRequest {
    @Schema(description = "ID of the book to rent", example = "5")
    private Long bookId;
}
