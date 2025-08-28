package com.example.bookify.controller;

import com.example.bookify.dto.BookRequest;
import com.example.bookify.dto.BookResponse;
import com.example.bookify.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Books", description = "Endpoints for managing books in the library")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Create a new book",
            description = "Adds a new book to the library collection.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Book successfully created",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data",
                            content = @Content(schema = @Schema(example = "{ \"error\": \"Title mustn't be blank\" }")))
            }
    )
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        log.info("POST /api/books - creating new book : '{}'", request.getTitle());
        BookResponse response = bookService.create(request);
        log.debug("Book created successfully with ID {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get all books",
            description = "Retrieves a list of all books available in the library.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of books",
                            content = @Content(schema = @Schema(implementation = BookResponse.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("GET /api/books - retrieving list of all books");
        List<BookResponse> responses = bookService.getAll();
        log.debug("{} books retrieved", responses.size());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "Get a book by ID",
            description = "Retrieves details of a specific book by its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book details retrieved",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found",
                            content = @Content(schema = @Schema(example = "{ \"error\": \"Book with id=1 not found\" }")))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        log.info("GET /api/books/{} - retrieving book details", id);
        BookResponse response = bookService.getById(id);
        log.debug("Book details retrieved: {}", response);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update a book",
            description = "Updates the details of an existing book.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Book successfully updated",
                            content = @Content(schema = @Schema(implementation = BookResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        log.info("PUT /api/books/{} - updating book with ID: {}", id, id);
        BookResponse response = bookService.update(id, request);
        log.debug("Book with ID {} updated", id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes a book from the library by its ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("DELETE /api/books/{} - deleting book", id);
        bookService.delete(id);
        log.debug("Book with ID {} successfully deleted", id);
        return ResponseEntity.noContent().build();
    }

}
