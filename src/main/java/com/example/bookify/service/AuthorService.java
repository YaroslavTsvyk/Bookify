package com.example.bookify.service;

import com.example.bookify.model.Author;

import java.util.List;

public interface AuthorService {
    Author create(Author author);
    Author getById(Long id);
    List<Author> getAll();
    Author update(Long id, Author updatedAuthor);
    void delete(Long id);
}
