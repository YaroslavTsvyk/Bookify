package com.example.bookify.dto;

import com.example.bookify.model.Category;
import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String description;
    private int publicationYear;

    private Category category;

    private String authorName;
    private boolean available;
}
