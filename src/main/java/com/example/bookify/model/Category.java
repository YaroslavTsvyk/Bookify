package com.example.bookify.model;


import io.swagger.v3.oas.annotations.media.Schema;

public enum Category {
    @Schema(description="Enum that describes fiction books")
    FICTION,
    @Schema(description="Enum that describes non-fiction books")
    NONFICTION,
    @Schema(description="Enum that describes science books")
    SCIENCE,
    @Schema(description="Enum that describes fantasy books")
    FANTASY,
    @Schema(description="Enum that describes history books")
    HISTORY,
    @Schema(description="Enum that describes biography books")
    BIOGRAPHY,
    @Schema(description="Enum that describes other books")
    OTHER
}
