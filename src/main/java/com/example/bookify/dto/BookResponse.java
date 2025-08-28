package com.example.bookify.dto;

import com.example.bookify.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @Schema(description = "Title of the book", example = "Clean Architecture")
    private String title;

    @Schema(description = "Description of the book", example = "A book about clean software architecture principles")
    private String description;

    @Schema(description = "Year of publication", example = "2017")
    private int publicationYear;

    @Schema(description = "Category of the book", example = "NONFICTION")
    private Category category;

    @Schema(description = "Availability status of the book", example = "true")
    private boolean available;

    @Schema(description = "Author's full name", example = "Robert C. Martin")
    private String authorName;
}
