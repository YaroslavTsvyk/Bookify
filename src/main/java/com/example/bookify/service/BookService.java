package com.example.bookify.service;

import com.example.bookify.dto.BookRequest;
import com.example.bookify.dto.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse create(BookRequest bookRequest);
    BookResponse getById(Long id);
    List<BookResponse> getAll();
    BookResponse update(Long id, BookRequest updatedBookRequest);
    void delete(Long id);
}
