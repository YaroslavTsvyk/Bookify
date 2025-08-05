package com.example.bookify.dto.mapper;

import com.example.bookify.dto.BookRequest;
import com.example.bookify.dto.BookResponse;
import com.example.bookify.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public Book toEntity(BookRequest bookRequest) {
        return Book.builder()
                .title(bookRequest.getTitle())
                .description(bookRequest.getDescription())
                .publicationYear(bookRequest.getPublicationYear())
                .authorName(bookRequest.getAuthorName())
                .available(bookRequest.isAvailable())
                .category(bookRequest.getCategory())
                .build();
    }

    public BookResponse toDto(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .publicationYear(book.getPublicationYear())
                .category(book.getCategory())
                .available(book.isAvailable())
                .authorName(book.getAuthorName())
                .build();
    }
}
