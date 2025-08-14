package com.example.bookify.controller;

import com.example.bookify.dto.BookRequest;
import com.example.bookify.dto.BookResponse;
import com.example.bookify.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        log.info("POST /api/books - creating new book : '{}'", request.getTitle());
        BookResponse response = bookService.create(request);
        log.debug("Book created successfully with ID {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("GET /api/books - retrieving list of all books");
        List<BookResponse> responses = bookService.getAll();
        log.debug("{} books retrieved", responses.size());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        log.info("GET /api/books/{} - retrieving book details", id);
        BookResponse response = bookService.getById(id);
        log.debug("Book details retrieved: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        log.info("PUT /api/books/{} - updating book with ID: {}", id, id);
        BookResponse response = bookService.update(id, request);
        log.debug("Book with ID {} updated", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("DELETE /api/books/{} - deleting book", id);
        bookService.delete(id);
        log.debug("Book with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

}
