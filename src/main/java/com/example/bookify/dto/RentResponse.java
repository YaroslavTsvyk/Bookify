package com.example.bookify.dto;

import com.example.bookify.model.RentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RentResponse {
    private Long id;
    private String bookTitle;
    private String userName;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private RentStatus status;
}
