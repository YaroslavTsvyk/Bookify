package com.example.bookify.service.impl;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;
import com.example.bookify.dto.mapper.RentMapper;
import com.example.bookify.model.Book;
import com.example.bookify.model.Rent;
import com.example.bookify.model.User;
import com.example.bookify.repository.BookRepository;
import com.example.bookify.repository.RentRepository;
import com.example.bookify.service.RentService;
import com.example.bookify.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + rentRequest.getBookId() + " not found"));

        // TODO after implementing auth correct this and other methods, which work with rentMapper.toEntity(...)
        User user = userService.getById(1L);

        Rent rent = rentMapper.toEntity(user, book);

        rentRepository.save(rent);
        return rentMapper.toDto(rent);
    }

    @Override
    @Transactional(readOnly = true)
    public RentResponse getById(Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rent with id " + id + " not found"));
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
                .orElseThrow(() -> new EntityNotFoundException("Rent with id " + id + " not found"));

        Book book = bookRepository.findById(updatedRentRequest.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + updatedRentRequest.getBookId() + " not found"));

        rent.setBook(book);
        rentRepository.save(rent);

        return rentMapper.toDto(rent);
    }

    @Override
    public void delete(Long id) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rent with id " + id + " not found"));
        rentRepository.delete(rent);
    }
}
