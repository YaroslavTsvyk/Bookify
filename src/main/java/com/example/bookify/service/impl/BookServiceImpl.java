package com.example.bookify.service.impl;

import com.example.bookify.model.Book;
import com.example.bookify.repository.BookRepository;
import com.example.bookify.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book update(Long id, Book updatedBook) {
        Book existing = getById(id);
        existing.setTitle(updatedBook.getTitle());
        existing.setDescription(updatedBook.getDescription());
        existing.setCategory(updatedBook.getCategory());
        existing.setAuthorName(updatedBook.getAuthorName());
        existing.setPublicationYear(updatedBook.getPublicationYear());
        existing.setAvailable(updatedBook.isAvailable());
        existing.setRents(updatedBook.getRents());
        return bookRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        bookRepository.delete(getById(id));
    }
}
