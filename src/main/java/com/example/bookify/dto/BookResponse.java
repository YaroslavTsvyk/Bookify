package com.example.bookify.dto;

import com.example.bookify.model.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String description;
    private int publicationYear;
    private Category category;
    private boolean available;
    private String authorName;
}
