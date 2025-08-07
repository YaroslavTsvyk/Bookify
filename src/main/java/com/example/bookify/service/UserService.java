package com.example.bookify.service;

import com.example.bookify.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User create(User user);
    User getById(Long id);
    List<User> getAll();
    User update(Long id, User updatedUser);
    void delete(Long id);
    UserDetails loadUserByUsername(String email);
}
