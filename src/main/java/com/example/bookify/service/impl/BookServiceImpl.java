package com.example.bookify.service.impl;

import com.example.bookify.dto.BookRequest;
import com.example.bookify.dto.BookResponse;
import com.example.bookify.dto.mapper.BookMapper;
import com.example.bookify.exception.ResourceNotFoundException;
import com.example.bookify.model.Book;
import com.example.bookify.repository.BookRepository;
import com.example.bookify.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse create(BookRequest bookRequest) {
        log.info("Creating new book with title='{}', author='{}'",
                bookRequest.getTitle(), bookRequest.getAuthorName());
        Book book = bookRepository.save(bookMapper.toEntity(bookRequest));
        log.debug("Book saved to database with id={}", book.getId());
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        log.info("Fetching book with id={}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        log.debug("Book with id={} retrieved successfully", id);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        log.info("Fetching all books");
        List<BookResponse> responses = bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
        log.debug("Fetched {} books from database", responses.size());
        return responses;
    }

    @Override
    public BookResponse update(Long id, BookRequest updatedBookRequest) {
        log.info("Updating book with id={}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));

        log.debug("Current state of book before update: {}", book);

        book.setTitle(updatedBookRequest.getTitle());
        book.setCategory(updatedBookRequest.getCategory());
        book.setDescription(updatedBookRequest.getDescription());
        book.setAuthorName(updatedBookRequest.getAuthorName());
        book.setAvailable(updatedBookRequest.isAvailable());
        book.setPublicationYear(updatedBookRequest.getPublicationYear());

        bookRepository.save(book);

        log.debug("Book with id={} updated successfully", id);

        return bookMapper.toDto(book);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting book with id={}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        bookRepository.delete(book);
        log.debug("Book with id={} deleted successfully", id);
    }
}
