package com.example.bookify.service;

import com.example.bookify.dto.RentRequest;
import com.example.bookify.dto.RentResponse;

import java.util.List;

public interface RentService {
    RentResponse create(RentRequest rentRequest);
    RentResponse getById(Long id);
    List<RentResponse> getAll();
    RentResponse update(Long id, RentRequest updatedRentRequest);
    void delete(Long id);
    List<RentResponse> getAllForUser();
    RentResponse returnBook(Long rentId);
}
