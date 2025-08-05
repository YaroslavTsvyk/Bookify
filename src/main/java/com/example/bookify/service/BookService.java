package com.example.bookify.service;

import com.example.bookify.model.Book;

import java.util.List;

public interface BookService {
    Book create(Book book);
    Book getById(Long id);
    List<Book> getAll();
    Book update(Long id, Book updatedBook);
    void delete(Long id);
}
