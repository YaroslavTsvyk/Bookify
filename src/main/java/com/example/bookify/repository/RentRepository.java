package com.example.bookify.repository;

import com.example.bookify.model.Rent;
import com.example.bookify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findAllByUser(User user);
}
