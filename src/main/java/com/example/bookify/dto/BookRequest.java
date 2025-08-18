package com.example.bookify.dto;

import com.example.bookify.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookRequest {

    @NotBlank(message = "Title mustn't be blank")
    @Size(max = 255, message = "Title must be shorter than 255 symbols")
    private String title;

    @NotBlank(message = "Description mustn't be blank")
    @Size(max = 2000, message = "Description must be shorter than 2000 symbols")
    private String description;

    @Min(value = 0, message = "Publication year mustn't be less than 0 (No books from B.C.)")
    private int publicationYear;

    @NotNull(message = "Category is required")
    private Category category;

    @NotBlank(message = "Author's name mustn't be blank")
    @Size(max = 255, message = "Author's name must be shorter than 255 symbols")
    private String authorName;

    private boolean available;
}
