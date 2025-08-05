package com.example.bookify.service.impl;

import com.example.bookify.model.Rent;
import com.example.bookify.repository.RentRepository;
import com.example.bookify.service.RentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentRepository rentRepository;

    @Override
    public Rent create(Rent rent) {
        return rentRepository.save(rent);
    }

    @Override
    public Rent getById(Long id) {
        return rentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rent with id " + id + " not found"));
    }

    @Override
    public List<Rent> getAll() {
        return rentRepository.findAll();
    }

    @Override
    public Rent update(Long id, Rent updatedRent) {
        Rent existing = getById(id);
        existing.setBook(updatedRent.getBook());
        existing.setRentDate(updatedRent.getRentDate());
        existing.setReturnDate(updatedRent.getReturnDate());
        existing.setUser(updatedRent.getUser());
        existing.setStatus(updatedRent.getStatus());
        return rentRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        rentRepository.delete(getById(id));
    }
}
