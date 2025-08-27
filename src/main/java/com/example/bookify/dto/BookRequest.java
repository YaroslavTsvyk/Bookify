package com.example.bookify.dto;

import com.example.bookify.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    @Schema(description = "Title of the book", example = "Clean Architecture")
    @NotBlank(message = "Title mustn't be blank")
    @Size(max = 255, message = "Title must be shorter than 255 symbols")
    private String title;

    @Schema(description = "Description of the book", example = "A book about clean software architecture principles")
    @NotBlank(message = "Description mustn't be blank")
    @Size(max = 2000, message = "Description must be shorter than 2000 symbols")
    private String description;

    @Schema(description = "Year of publication", example = "2017")
    @Min(value = 0, message = "Publication year mustn't be less than 0 (No books from B.C.)")
    private int publicationYear;

    @Schema(description = "Category of the book", example = "NONFICTION")
    @NotNull(message = "Category is required")
    private Category category;

    @Schema(description = "Author's full name", example = "Robert C. Martin")
    @NotBlank(message = "Author's name mustn't be blank")
    @Size(max = 255, message = "Author's name must be shorter than 255 symbols")
    private String authorName;

    @Schema(description = "Availability status of the book", example = "true")
    private boolean available;
}
