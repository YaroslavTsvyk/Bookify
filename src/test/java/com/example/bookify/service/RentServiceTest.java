package com.example.bookify.service;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;
import com.example.bookify.dto.mapper.RentMapper;
import com.example.bookify.exception.BookAlreadyReturnedException;
import com.example.bookify.exception.BookUnavailableException;
import com.example.bookify.exception.ResourceNotFoundException;
import com.example.bookify.model.*;
import com.example.bookify.repository.BookRepository;
import com.example.bookify.repository.RentRepository;
import com.example.bookify.service.impl.RentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentServiceTest {
    @Mock private RentRepository rentRepository;
    @Mock private BookRepository bookRepository;
    @Mock private UserService userService;
    @Mock private RentMapper rentMapper;
    @InjectMocks private RentServiceImpl rentService;

    // Test data
    private RentRequest rentRequest;
    private Rent rent;
    private RentResponse rentResponse;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        rentRequest = new RentRequest(1L);

        book =  Book.builder()
                .id(1L)
                .title("Java Basics")
                .description("Beginner-friendly Java guide")
                .publicationYear(2023)
                .category(Category.NONFICTION)
                .available(true)
                .authorName("John Doe")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Reed")
                .email("jane@example.com")
                .build();

        rent = Rent.builder()
                .id(1L)
                .book(book)
                .user(user)
                .rentDate(LocalDate.now())
                .status(RentStatus.ACTIVE)
                .build();

        rentResponse = RentResponse.builder()
                .id(rent.getId())
                .bookTitle(book.getTitle())
                .userName(user.getUsername())
                .rentDate(rent.getRentDate())
                .status(RentStatus.ACTIVE)
                .build();
    }

    @Test
    void create_shouldSaveAndReturnResponse() {
        when(bookRepository.findById(rentRequest.getBookId())).thenReturn(Optional.of(book));
        when(userService.getCurrentUser()).thenReturn(user);
        when(rentMapper.toEntity(user, book)).thenReturn(rent);
        when(rentMapper.toDto(rent)).thenReturn(rentResponse);

        RentResponse result = rentService.create(rentRequest);

        verify(rentMapper).toEntity(user, book);
        verify(rentMapper).toDto(rent);
        verify(rentRepository).save(rent);
        verify(bookRepository).save(book);
        assertThat(result).isEqualTo(rentResponse);
    }

    @Test
    void create_shouldThrowIfBookNotFound() {
        when(bookRepository.findById(rentRequest.getBookId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentService.create(rentRequest))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book with id " + rentRequest.getBookId() + " not found");

        verify(rentRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowIfBookNotAvailable() {
        book.setAvailable(false);
        when(bookRepository.findById(rentRequest.getBookId())).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> rentService.create(rentRequest))
                .isExactlyInstanceOf(BookUnavailableException.class)
                .hasMessageContaining("Book with id " + book.getId() + " is unavailable at the moment");

        verify(rentRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnResponse() {
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));
        when(rentMapper.toDto(rent)).thenReturn(rentResponse);

        RentResponse result = rentService.getById(rent.getId());

        verify(rentMapper).toDto(rent);
        assertThat(result).isEqualTo(rentResponse);
    }

    @Test
    void getById_shouldThrowIfNotFound() {
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentService.getById(rent.getId()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Rent with id " + rent.getId() + " not found");
    }

    @Test
    void getAll_shouldReturnListOfResponses() {
        List<Rent> rents = List.of(rent);

        when(rentRepository.findAll()).thenReturn(rents);
        when(rentMapper.toDto(rent)).thenReturn(rentResponse);

        List<RentResponse> result = rentService.getAll();

        assertThat(result).hasSize(1).containsExactly(rentResponse);

        verify(rentMapper).toDto(rent);
    }

    @Test
    void getAllForUser_shouldReturnListOfResponses() {
        List<Rent> rents = List.of(rent);

        when(userService.getCurrentUser()).thenReturn(user);
        when(rentRepository.findAllByUser(user)).thenReturn(rents);
        when(rentMapper.toDto(rent)).thenReturn(rentResponse);

        List<RentResponse> result = rentService.getAllForUser();

        assertThat(result).hasSize(1).containsExactly(rentResponse);

        verify(rentMapper).toDto(rent);
    }

    @Test
    void update_shouldUpdateAndReturnResponse() {
        Book updatedBook = Book.builder()
                .id(2L)
                .title("1984")
                .description("Dystopian novel by George Orwell")
                .publicationYear(1949)
                .category(Category.FICTION)
                .available(true)
                .authorName("George Orwell")
                .build();

        RentRequest updatedRentRequest = new RentRequest(updatedBook.getId());

        rentResponse.setBookTitle(updatedBook.getTitle());

        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));
        when(bookRepository.findById(updatedRentRequest.getBookId())).thenReturn(Optional.of(updatedBook));
        when(rentMapper.toDto(rent)).thenReturn(rentResponse);

        RentResponse result = rentService.update(rent.getId(), updatedRentRequest);

        assertThat(result).isEqualTo(rentResponse);

        verify(rentRepository).save(rent);
        verify(rentMapper).toDto(rent);
    }

    @Test
    void update_shouldThrowIfRentNotFound() {
        RentRequest updatedRentRequest = new RentRequest(book.getId());

        when(rentRepository.findById(rent.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentService.update(rent.getId(), updatedRentRequest))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Rent with id " + rent.getId() + " not found");

        verify(rentRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowIfBookNotFound() {
        RentRequest updatedRentRequest = new RentRequest(book.getId());

        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));
        when(bookRepository.findById(updatedRentRequest.getBookId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentService.update(rent.getId(), updatedRentRequest))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Book with id " + updatedRentRequest.getBookId() + " not found");

        verify(rentRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteIfRentFound() {
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));

        rentService.delete(rent.getId());

        verify(rentRepository).delete(rent);
    }

    @Test
    void delete_shouldThrowIfRentNotFound() {
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentService.delete(rent.getId()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Rent with id " + rent.getId() + " not found");

        verify(rentRepository, never()).delete(any());
    }

    @Test
    void return_shouldReturnBookAndReturnResponse() {
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));
        when(userService.getCurrentUser()).thenReturn(user);
        when(rentMapper.toDto(rent)).thenReturn(rentResponse);

        RentResponse result = rentService.returnBook(rent.getId());

        verify(bookRepository).save(book);
        verify(rentRepository).save(rent);
        verify(rentMapper).toDto(rent);

        assertThat(result).isEqualTo(rentResponse);
    }

    @Test
    void return_shouldThrowIfRentNotFound() {
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rentService.returnBook(rent.getId()))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Rent with id " + rent.getId() + " not found");

        verify(rentRepository, never()).save(any());
    }

    @Test
    void return_shouldThrowIfRentNotBelongToUser() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));
        when(userService.getCurrentUser()).thenReturn(anotherUser);

        assertThatThrownBy(() -> rentService.returnBook(rent.getId()))
                .isExactlyInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("User with id " + anotherUser.getId() + " is not allowed to return this book");

        verify(rentRepository, never()).save(any());
    }

    @Test
    void return_shouldThrowIfBookIsReturned() {
        rent.setStatus(RentStatus.RETURNED);
        when(rentRepository.findById(rent.getId())).thenReturn(Optional.of(rent));
        when(userService.getCurrentUser()).thenReturn(user);

        assertThatThrownBy(() -> rentService.returnBook(rent.getId()))
                .isExactlyInstanceOf(BookAlreadyReturnedException.class)
                .hasMessageContaining("Book with id " + rent.getBook().getId() + " is already returned");

        verify(rentRepository, never()).save(any());
    }
}
