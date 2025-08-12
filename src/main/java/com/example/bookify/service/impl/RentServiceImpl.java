package com.example.bookify.service.impl;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;
import com.example.bookify.dto.mapper.RentMapper;
import com.example.bookify.exception.BookAlreadyReturnedException;
import com.example.bookify.exception.BookUnavailableException;
import com.example.bookify.exception.ResourceNotFoundException;
import com.example.bookify.model.Book;
import com.example.bookify.model.Rent;
import com.example.bookify.model.RentStatus;
import com.example.bookify.model.User;
import com.example.bookify.repository.BookRepository;
import com.example.bookify.repository.RentRepository;
import com.example.bookify.service.RentService;
import com.example.bookify.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final RentMapper rentMapper;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    public RentResponse create(RentRequest rentRequest) {
        Book book = bookRepository.findById(rentRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + rentRequest.getBookId() + " not found"));

        if (!book.isAvailable()) {
            throw new BookUnavailableException("This book is unavailable at the moment");
        }

        User user = userService.getCurrentUser();

        book.setAvailable(false);

        Rent rent = rentMapper.toEntity(user, book);

        rentRepository.save(rent);
        bookRepository.save(book);
        return rentMapper.toDto(rent);
    }

    @Override
    @Transactional(readOnly = true)
    public RentResponse getById(Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id " + id + " not found"));
        return rentMapper.toDto(rent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getAll() {
        return rentRepository.findAll()
                .stream()
                .map(rentMapper::toDto)
                .toList();
    }

    @Override
    public RentResponse update(Long id, RentRequest updatedRentRequest) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id " + id + " not found"));

        Book book = bookRepository.findById(updatedRentRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + updatedRentRequest.getBookId() + " not found"));

        rent.setBook(book);
        rentRepository.save(rent);

        return rentMapper.toDto(rent);
    }

    @Override
    public void delete(Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id " + id + " not found"));
        rentRepository.delete(rent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getAllForUser() {
        User user = userService.getCurrentUser();
        return rentRepository.findAllByUser(user)
                .stream()
                .map(rentMapper::toDto)
                .toList();
    }

    @Override
    public RentResponse returnBook(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new ResourceNotFoundException("Rent not found"));

        User currentUser = userService.getCurrentUser();

        if (!rent.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to return this book");
        }

        if (rent.getStatus() == RentStatus.RETURNED) {
            throw new BookAlreadyReturnedException("Book is already returned");
        }

        Book book = rent.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        rent.setReturnDate(LocalDate.now());
        rent.setStatus(RentStatus.RETURNED);

        rentRepository.save(rent);

        return rentMapper.toDto(rent);
    }
}
