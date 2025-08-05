package com.example.bookify.service;

import com.example.bookify.model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    User getById(Long id);
    List<User> getAll();
    User update(Long id, User updatedUser);
    void delete(Long id);
}
