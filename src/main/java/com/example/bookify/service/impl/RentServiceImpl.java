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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;
    private final RentMapper rentMapper;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    public RentResponse create(RentRequest rentRequest) {
        log.info("Creating rent for bookId={} by current user", rentRequest.getBookId());

        Book book = bookRepository.findById(rentRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " +
                        rentRequest.getBookId() + " not found"));

        if (!book.isAvailable()) {
            throw new BookUnavailableException("This book is unavailable at the moment");
        }

        User user = userService.getCurrentUser();
        log.debug("Current user for rent: {}", user.getEmail());

        book.setAvailable(false);

        Rent rent = rentMapper.toEntity(user, book);
        log.debug("Rent saved to database with id={}", rent.getId());

        rentRepository.save(rent);
        bookRepository.save(book);
        log.debug("Book with id={} rented successfully by userId={}", book.getId(), user.getId());

        return rentMapper.toDto(rent);
    }

    @Override
    @Transactional(readOnly = true)
    public RentResponse getById(Long id) {
        log.info("Fetching rent with id={}", id);
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id " + id + " not found"));
        log.debug("Fetched rent details: {}", rent);
        return rentMapper.toDto(rent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getAll() {
        log.info("Fetching all rents");
        List<RentResponse> responses = rentRepository.findAll()
                .stream()
                .map(rentMapper::toDto)
                .toList();
        log.debug("Total rents fetched: {}", responses.size());
        return responses;
    }

    @Override
    public RentResponse update(Long id, RentRequest updatedRentRequest) {
        log.info("Updating rent with id={}", id);

        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rent with id={} not found", id);
                    return new ResourceNotFoundException("Rent with id " + id + " not found");
                });

        Book book = bookRepository.findById(updatedRentRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " +
                        updatedRentRequest.getBookId() + " not found"));

        rent.setBook(book);
        rentRepository.save(rent);

        log.debug("Rent with id={} updated successfully", id);
        return rentMapper.toDto(rent);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting rent with id={}", id);
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id " + id + " not found"));
        rentRepository.delete(rent);
        log.info("Rent with id={} deleted successfully", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentResponse> getAllForUser() {
        User user = userService.getCurrentUser();
        log.info("Fetching all rents for userId={}", user.getId());
        List<RentResponse> responses = rentRepository.findAllByUser(user)
                .stream()
                .map(rentMapper::toDto)
                .toList();
        log.debug("User with id={} has {} rents", user.getId(), responses.size());
        return responses;
    }

    @Override
    public RentResponse returnBook(Long rentId) {
        log.info("Returning book for rentId={}", rentId);

        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new ResourceNotFoundException("Rent with id " + rentId + " not found"));

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
        log.debug("Book with id={} set as available", book.getId());

        rent.setReturnDate(LocalDate.now());
        rent.setStatus(RentStatus.RETURNED);

        rentRepository.save(rent);
        log.debug("Book with id={} successfully returned for rentId={}", book.getId(), rentId);

        return rentMapper.toDto(rent);
    }
}
