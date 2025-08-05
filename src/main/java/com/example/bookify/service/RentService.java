package com.example.bookify.service;

import com.example.bookify.model.Rent;

import java.util.List;

public interface RentService {
    Rent create(Rent rent);
    Rent getById(Long id);
    List<Rent> getAll();
    Rent update(Long id, Rent updatedRent);
    void delete(Long id);
}
