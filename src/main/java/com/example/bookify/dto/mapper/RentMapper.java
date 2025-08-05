package com.example.bookify.dto.mapper;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;
import com.example.bookify.model.Book;
import com.example.bookify.model.Rent;
import com.example.bookify.model.RentStatus;
import com.example.bookify.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RentMapper {
    public Rent toEntity(RentRequest dto, User user, Book book) {
        return Rent.builder()
                .book(book)
                .user(user)
                .status(RentStatus.ACTIVE)
                .rentDate(LocalDate.now())
                .build();
    }

    public RentResponse toDto(Rent rent) {
        return RentResponse.builder()
                .id(rent.getId())
                .bookTitle(rent.getBook().getTitle())
                .userName(rent.getUser().getUsername())
                .rentDate(rent.getRentDate())
                .returnDate(rent.getReturnDate())
                .status(rent.getStatus())
                .build();
    }
}
