package com.example.bookify.service.impl;

import com.example.bookify.dto.BookRequest;
import com.example.bookify.dto.BookResponse;
import com.example.bookify.dto.mapper.BookMapper;
import com.example.bookify.model.Book;
import com.example.bookify.repository.BookRepository;
import com.example.bookify.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse create(BookRequest bookRequest) {
        Book book = bookRepository.save(bookMapper.toEntity(bookRequest));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookResponse update(Long id, BookRequest updatedBookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));

        book.setTitle(updatedBookRequest.getTitle());
        book.setCategory(updatedBookRequest.getCategory());
        book.setDescription(updatedBookRequest.getDescription());
        book.setAuthorName(updatedBookRequest.getAuthorName());
        book.setAvailable(updatedBookRequest.isAvailable());
        book.setPublicationYear(updatedBookRequest.getPublicationYear());

        bookRepository.save(book);

        return bookMapper.toDto(book);
    }

    @Override
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
        bookRepository.delete(book);
    }
}
