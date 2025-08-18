package com.example.bookify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RentRequest {
    private Long bookId;
}
