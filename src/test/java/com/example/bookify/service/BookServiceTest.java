package com.example.bookify.service;

import com.example.bookify.dto.BookRequest;
import com.example.bookify.dto.BookResponse;
import com.example.bookify.dto.mapper.BookMapper;
import com.example.bookify.exception.ResourceNotFoundException;
import com.example.bookify.model.Book;
import com.example.bookify.model.Category;
import com.example.bookify.repository.BookRepository;
import com.example.bookify.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock private BookRepository bookRepository;
    @Mock private BookMapper bookMapper;
    @InjectMocks private BookServiceImpl bookService;

    // Test data
    private BookRequest bookRequest;
    private Book book;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        bookRequest = new BookRequest(
                "Java Basics",
                "Beginner-friendly Java guide",
                2023,
                Category.NONFICTION,
                "John Doe",
                true
        );

        book = Book.builder()
                .id(1L)
                .title(bookRequest.getTitle())
                .description(bookRequest.getDescription())
                .publicationYear(bookRequest.getPublicationYear())
                .category(bookRequest.getCategory())
                .available(bookRequest.isAvailable())
                .authorName(bookRequest.getAuthorName())
                .build();

        bookResponse = BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .publicationYear(book.getPublicationYear())
                .category(book.getCategory())
                .available(book.isAvailable())
                .authorName(book.getAuthorName())
                .build();
    }

    @Test
    void create_shouldSaveAndReturnResponse() {
        when(bookMapper.toEntity(bookRequest)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookResponse);

        BookResponse result = bookService.create(bookRequest);

        assertThat(result).isEqualTo(bookResponse);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }


    @Test
    void getById_shouldReturnResponseIfBookFound() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookResponse);

        BookResponse result = bookService.getById(book.getId());

        assertThat(result).isEqualTo(bookResponse);
        verify(bookMapper).toDto(book);
    }

    @Test
    void getById_shouldThrowIfBookNotFound() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getById(book.getId()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book with id " + book.getId() + " not found");
    }

    @Test
    void getAll_shouldReturnListOfResponses() {
        List<Book> books = List.of(book);

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDto(book)).thenReturn(bookResponse);

        List<BookResponse> result = bookService.getAll();

        assertThat(result).hasSize(1).containsExactly(bookResponse);

        verify(bookRepository).findAll();
        verify(bookMapper).toDto(book);
    }


    @Test
    void update_shouldUpdateAndReturnResponse() {
        BookRequest updatedRequest = new BookRequest(
                "Advanced Java",
                "Deep dive into Java",
                2024,
                Category.NONFICTION,
                "Jane Smith",
                false
        );

        BookResponse updatedResponse = BookResponse.builder()
                .id(book.getId())
                .title(updatedRequest.getTitle())
                .description(updatedRequest.getDescription())
                .publicationYear(updatedRequest.getPublicationYear())
                .category(updatedRequest.getCategory())
                .authorName(updatedRequest.getAuthorName())
                .available(updatedRequest.isAvailable())
                .build();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(updatedResponse);

        BookResponse result = bookService.update(book.getId(), updatedRequest);

        assertThat(result).isEqualTo(updatedResponse);

        assertThat(book.getTitle()).isEqualTo(updatedRequest.getTitle());
        assertThat(book.getDescription()).isEqualTo(updatedRequest.getDescription());
        assertThat(book.getAuthorName()).isEqualTo(updatedRequest.getAuthorName());
        assertThat(book.getPublicationYear()).isEqualTo(updatedRequest.getPublicationYear());
        assertThat(book.isAvailable()).isEqualTo(updatedRequest.isAvailable());
        assertThat(book.getCategory()).isEqualTo(updatedRequest.getCategory());

        verify(bookRepository).save(book);
        verify(bookRepository).findById(book.getId());
        verify(bookMapper).toDto(book);
    }

    @Test
    void update_shouldThrowIfBookNotFound() {
        BookRequest updatedRequest = new BookRequest(
                "Advanced Java",
                "Deep dive into Java",
                2024,
                Category.NONFICTION,
                "Jane Smith",
                false
        );

        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.update(book.getId(), updatedRequest))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book with id " + book.getId() + " not found");

        verify(bookRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteIfBookFound() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.delete(book.getId());

        verify(bookRepository).delete(book);
    }

    @Test
    void delete_shouldThrowIfBookNotFound() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.delete(book.getId()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book with id " + book.getId() + " not found");

        verify(bookRepository, never()).delete(any());
    }

}
